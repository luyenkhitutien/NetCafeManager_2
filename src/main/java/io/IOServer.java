package io;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import dao.*;
import entity.*;
import java.awt.Color;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import com.mycompany.netcafemanager.MainTest;
import test_server_client_GUI.ServerChatGUI;
import utils.Auth;
import utils.XDate;

public class IOServer {

    public static final int PORT = 1235;
    public static final ConcurrentHashMap<Integer, ClientHandler> loggedInClientsMap = new ConcurrentHashMap<>();
    private final ServerSocket serverSocket;
    private final int employeeId;
    private ServerChatGUI chatGUI;
    private final Date startTimeServer = XDate.now();
    private Date endTimeServer;

    public IOServer(int employeeId) throws IOException {
        this.employeeId = employeeId;
        serverSocket = new ServerSocket(PORT);
    }

    public Map<Integer, ClientHandler> getLoggedInClientsMap() {
        return loggedInClientsMap;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setChatGUI(ServerChatGUI chatGUI) {
        this.chatGUI = chatGUI;
    }
    public ClientHandler getClientHandler(int clientId) {
        return loggedInClientsMap.get(clientId);
    }


    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    if (serverSocket.isClosed()) {
                        System.out.println("Server socket is closed. Stopping server thread.");
                        break;
                    }

                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(this, socket);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        System.out.println("Server socket was closed. Exiting server thread.");
                        break;
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Account getAccountByClientID(int clientID) {
        ClientHandler clientHandler = loggedInClientsMap.get(clientID);
        if (clientHandler != null) {
            return clientHandler.getAccount();
        }
        return null;
    }

    public void shutdownServer() {
        try {
            try (serverSocket) {
                this.sendMessageToAllClients("SHUTDOWN");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized String closeClientByComputerID(int knownComputerID) throws IOException {

        if (knownComputerID != 0) {
            sendMessageToClient(knownComputerID, "SHUTDOWN");
            return "Successfully shutdown the client";
        } else {
            return "Client with computer ID " + knownComputerID + " not found.";
        }
    }

    public synchronized String openForGuest(int knownComputerID) throws IOException {

        if (knownComputerID != 0) {
            sendMessageToClient(knownComputerID, "OPEN_GUEST");
            return "Successfully open computer for guest";
        } else {
            return "Client with computer ID " + knownComputerID + " not found.";
        }
    }

    public synchronized void sendMessageToClient(int clientID, String message) throws IOException {
        ClientHandler clientHandler = loggedInClientsMap.get(clientID);
        if (clientHandler != null) {
            clientHandler.sendMessage(message);
        } else {
            System.out.println("Client with ID " + clientID + " not found.");
        }
    }

    public synchronized void sendMessageToAllClients(String message) throws IOException {
        for (ClientHandler clientHandler : loggedInClientsMap.values()) {
            if(clientHandler != null){
                clientHandler.sendMessage(message);
            }else{
                return;
            }
            
        }
    }

    public synchronized void calculateBalanceForEmloyee() {
        try {
            endTimeServer = XDate.now();

            EmployeeDAO employeeDAO = new EmployeeDAO();
            Employee employee = employeeDAO.selectByID(employeeId);

            BigDecimal hours = XDate.getDifferenceInHours(endTimeServer, startTimeServer);
            BigDecimal totalMoney = employee.getSalaryPerHour().multiply(hours);

            BigDecimal balanceBefore = employee.getBalance();

            BigDecimal newBalance = balanceBefore.add(totalMoney);

            employee.setBalance(newBalance);
            employeeDAO.update(employee);

            System.out.println(employee.getBalance());
        } catch (Exception ex) {
            Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static class ClientHandler extends Thread {

        private volatile boolean running = true;
        private final Socket socket;
        private final IOServer server;
        private int clientID = 0;
        private int computerID;
        private Computer computer;
        private Account account;
        private Session session;
        private Invoice invoice;
        private Member member;
        private InvoiceDetail invoiceDetail;
        private Product product;
        private final ObjectOutputStream out;
        private final ObjectInputStream in;

        AccountDAO accountDAO = new AccountDAO();
        MemberDAO memberDAO = new MemberDAO();
        ComputerDAO computerDAO = new ComputerDAO();
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        SessionDAO sessionDAO = new SessionDAO();
        InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();
        ProductDAO productDAO = new ProductDAO();

        public ClientHandler(IOServer server, Socket socket) throws IOException {
            this.server = server;
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        }

        public int getClientID() {
            return clientID;
        }

        public int getComputerID() {
            return computerID;
        }
        
        public Account getAccount(){
            return account;
        }

        private void sendMessage(String message) throws IOException {
            out.writeObject(message);
            out.flush(); // Ensure the message is sent immediately
        }

        public void run() {
            while (running) {
                try {
                    String request = (String) in.readObject();
                    String[] parts = request.split(";");
                    String command = parts[0];

                    switch (command) {
                        case "WAITING_OPEN" -> {
                            computerID = Integer.parseInt(parts[1]);
                            String response = waitingOpen();
                            sendResponse("RESPONSE_TEXT", response);
                        }
                        case "GUEST" -> {
                            String response = openForGuest();
                            sendResponse("RESPONSE_TEXT", response);
                        }
                        case "LOGIN" -> {
                            String username = parts[1];
                            String password = parts[2];
                            String response = login(username, password);
                            sendResponse("RESPONSE_TEXT", response);
                        }
                        case "CHANGE_PASSWORD" -> {
                            String newPassword = parts[1];
                            String response = changePassword(newPassword);
                            sendResponse("RESPONSE_TEXT", response);
                        }
                        case "ORDER_PRODUCT" -> {
                            String productName = parts[1];
                            int quantity = Integer.parseInt(parts[2]);
                            String response = orderProduct(productName, quantity);
                            sendResponse("RESPONSE_TEXT", response);
                        }
                        case "LOGOUT" -> {
                            logout();
                            reset();
                        }
                        case "MAINTENANCE" -> {
                            String response = toMaintain();
                            sendResponse("RESPONSE_TEXT", response);
                        }
                        case "SEND_MESSAGE" -> {
                            String mess = parts[1];
                            receiveMessage(clientID, mess);
                            // Assuming no response needed for sending message
                        }
                        case "IMPORT_LIST_PRODUCT" -> {
                            List<Product> products = exportListProduct();
                            sendResponse("RESPONSE_PRODUCT_LIST", products);
                        }
                        case "GET_BALANCE" -> {
                            List<BigDecimal> listBigDecimals = getClientBalance();
                            sendResponse("RESPONSE_BALANCE", listBigDecimals);
                        }
                        case "SHUTDOWN" -> {
                            removeClient();
                            reset();
                            shutdown();
                        }
                        default -> {
                            sendResponse("RESPONSE_TEXT", "Invalid request");
                        }
                    }
                } catch (SocketException e) {
                    System.out.println("Socket closed, stopping  for ComputerID: " + computerID);
                    MainTest.mainForm.home.updateLabelColor(computer.getId(), null);
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private synchronized void receiveMessage(int clientID, String message) throws SQLException {
            String fullMessage = "Message from client " + clientID + ": " + message;
            System.out.println(fullMessage);
            if (server.chatGUI != null) {
                server.chatGUI.appendMessage(clientID, message);
            }
        }

        private synchronized String waitingOpen() throws Exception {
            computer = computerDAO.selectByID(computerID);
            clientID = computerID;
            IOServer.loggedInClientsMap.put(clientID, this);
            if (computer == null) {
                throw new Exception("Invalid computer ID: " + computerID);
            }
            computer.setStatus("Đang chờ");
            computerDAO.update(computer);
            MainTest.mainForm.home.updateLabelColor(computer.getId(), Color.YELLOW);

            return "Computer " + computerID + " waiting open..........";
        }

        private synchronized String toMaintain() throws Exception {
            if (computerID == 0) {
                return "Computer ID is not set. Please wait for the computer to open.";
            }
            computer.setStatus("Đang bảo trì");
            computerDAO.update(computer);
            MainTest.mainForm.home.updateLabelColor(computer.getId(), Color.RED);

            return "Computer " + computerID + " successfully to maintain";
        }

        public synchronized String openForGuest() throws Exception {
            if (computerID == 0) {
                return "Computer ID is not set. Please wait for the computer to open.";
            }

            computer.setStatus("Đang sử dụng");
            computerDAO.update(computer);
            MainTest.mainForm.home.updateLabelColor(computer.getId(), Color.GREEN);
            // Các xử lý khác liên quan đến mở máy cho khách vãng lai
            invoice = new Invoice(null, server.getEmployeeId(), BigDecimal.ZERO, XDate.now(), "Chưa hoàn thành");
            invoiceDAO.insert(invoice);

            session = new Session(computerID, XDate.now(), null, BigDecimal.ZERO, invoice.getId());
            sessionDAO.insert(session);
            return "Opened for guest successfully with client ID: " + clientID;
        }

        private synchronized String login(String username, String password) throws SQLException, Exception {
            if (computerID == 0) {
                return "Computer ID is not set. Please wait for the computer to open.";
            }

            account = accountDAO.selectByUsernameAndPassword(username, password);
            if (account != null && Auth.isMembers(account)) {

                computer.setStatus("Đang sử dụng");
                computerDAO.update(computer);
                MainTest.mainForm.home.updateLabelColor(computer.getId(), Color.GREEN);

                member = memberDAO.selectByAccountID(account.getId());
                
                invoice = new Invoice(member.getId(), server.getEmployeeId(), BigDecimal.ZERO, XDate.now(), "Chưa hoàn thành");
                invoiceDAO.insert(invoice);

                session = new Session(computerID, XDate.now(), null, BigDecimal.ZERO, invoice.getId());
                sessionDAO.insert(session);

                return "Login successful with client ID: " + clientID;
            } else {
                return "Invalid credentials";
            }
        }

        private List<BigDecimal> getClientBalance() {
            try {
                Member mem;
                if (member != null) {
                    mem = memberDAO.selectByID(member.getId());
                } else {
                    mem = null;
                }

                BigDecimal priceComputer = computer.getPricePerHour();
                BigDecimal priceComputerGuest = priceComputer.add(BigDecimal.valueOf(3000));
                List<BigDecimal> listBigDecimals = new ArrayList<>();

                if (mem != null) {
                    BigDecimal balanceClient = mem.getBalance();
                    listBigDecimals.add(0, balanceClient);
                    listBigDecimals.add(1, priceComputer);
                } else {
                    listBigDecimals.add(0, BigDecimal.ZERO); //Khách vãng lai không có số dư => mặc định là 0 
                    listBigDecimals.add(1, priceComputerGuest); // Giá máy +3000đ
                }

                return listBigDecimals;
            } catch (Exception ex) {
                Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        private synchronized String changePassword(String newPassword) throws SQLException, Exception {
            if (computerID == 0) {
                return "Computer ID is not set. Please wait for the computer to open.";
            }
            if (account != null) {
                account.setPassword(newPassword);
                accountDAO.update(account);
                return "Password changed successfully";
            }
            return "Error changing password";
        }

        private synchronized String orderProduct(String productName, int quantity) throws SQLException, Exception {
            if (computerID == 0) {
                return "Computer ID is not set. Please wait for the computer to open.";
            }
            if (clientID == 0 || server.getEmployeeId() == 0) {
                return "Not logged in";
            }

            product = productDAO.selectByName(productName);

            invoiceDetail = new InvoiceDetail();
            invoiceDetail.setCompleted(false);
            invoiceDetail.setProductID(product.getId());
            invoiceDetail.setInvoiceID(invoice.getId());
            invoiceDetail.setQuantity(quantity);
            invoiceDetail.setPrice(product.getPrice().multiply(new BigDecimal(quantity)));

            invoiceDetailDAO.insert(invoiceDetail);

            BigDecimal thanhTien = invoiceDetail.getPrice();
            BigDecimal tongGia = invoice.getTotalAmount().add(thanhTien);
            invoice.setTotalAmount(tongGia);
            invoiceDAO.update(invoice);

            return "Product ordered successfully and invoiceDetail created";
        }

        private synchronized List<Product> exportListProduct() throws Exception {
            return productDAO.selectAll();
        }

        private synchronized void logout() throws Exception {

            if (computerID == 0) {
                System.out.println("Computer ID is not set. Please wait for the computer to open.");
            }

            // Đảm bảo rằng session không null
            if (session == null) {
                throw new Exception("Session not found. Cannot log out.");
            }

            // Cập nhật thời gian kết thúc phiên
            session.setEndTime(XDate.now());
            System.out.println("Session end time set to: " + session.getEndTime());

            computer.setStatus("Đang chờ");
            computerDAO.update(computer);
            MainTest.mainForm.home.updateLabelColor(computer.getId(), Color.YELLOW);

            BigDecimal hours = XDate.getDifferenceInHours(session.getEndTime(), session.getStartTime());
            BigDecimal totalMoneyUsage = computer.getPricePerHour().multiply(hours).multiply(BigDecimal.TEN);

            List<InvoiceDetail> invoiceDetails = invoiceDetailDAO.selectByInvoiceID(invoice.getId());
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (InvoiceDetail detail : invoiceDetails) {
                totalPrice = totalPrice.add(detail.getPrice());
            }

            session.setTotalAmount(totalMoneyUsage);
            invoice.setTotalAmount(totalMoneyUsage.add(totalPrice));

            if (member != null) {
                Member mem = memberDAO.selectByID(member.getId());
                if (mem != null) {
                    BigDecimal balanceBefore = mem.getBalance();
                    BigDecimal totalAmount = invoice.getTotalAmount();
                    System.out.println("Balance Before: " + balanceBefore);
                    System.out.println("Total Amount: " + totalAmount);

                    BigDecimal newBalance = balanceBefore.subtract(totalAmount);
                    mem.setBalance(newBalance);
                    memberDAO.update(mem);

                    System.out.println("New Balance: " + newBalance);
                }
            }

            sessionDAO.update(session);
            invoiceDAO.update(invoice);

            System.out.println("Client " + clientID + " Logged out successfully");
        }

        private synchronized void reset() throws Exception {
            session = null;
            invoice = null;
        }

        private synchronized void removeClient() {
            IOServer.loggedInClientsMap.remove(clientID);
            clientID = 0;
        }

        public synchronized void shutdown() {
            computer.setStatus("Đang tắt");
            MainTest.mainForm.home.updateLabelColor(computer.getId(), UIManager.getColor("Component.background"));
            try {
                computerDAO.update(computer);
                MainTest.mainForm.home.updateLabelColor(computer.getId(), null);
            } catch (Exception ex) {
                Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(IOServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void sendResponse(String responseType, Object response) throws IOException {
            out.writeObject(responseType);
            out.flush();
            out.writeObject(response);
            out.flush();
        }
    }
}
