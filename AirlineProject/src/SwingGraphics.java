import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.http.HttpRequest;
import java.net.*;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import org.json.*;



public class SwingGraphics implements ActionListener {
    private JFrame mainFrame;

    private JLabel destLabel;
    private JLabel oriLabel;

    private JPanel topPanel;
    private JPanel destPanel;
    private JPanel oriPanel;
    private JPanel textPanel;

    private JButton goButton;

    private JTextArea ta;
    private JTextField dest;
    private JTextField ori;

    private int WIDTH=800;
    private int HEIGHT=700;

    String url;

    public SwingGraphics() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("HTML Scraper");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        //Instantiate Panels
        textPanel = new JPanel();
        topPanel = new JPanel();
        destPanel = new JPanel();
        oriPanel = new JPanel();
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,1));

        //text area
        textPanel.setLayout(new GridLayout());
        ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Poppins",Font.PLAIN,12));
        //set scrollable
        JScrollPane scroll = new JScrollPane(ta);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textPanel.add(scroll);
        //color
        //ta.setBackground(Color.black);
        //ta.setForeground(Color.GREEN);

        //button
        goButton = new JButton("GO");
        goButton.setFont(new Font("Poppins",Font.BOLD,20));
        goButton.setPreferredSize(new Dimension(200,50));
        goButton.setActionCommand("GO");
        goButton.addActionListener(new ButtonClickListener());

        //set labels
        oriLabel = new JLabel("Origin:", JLabel.CENTER);
        destLabel = new JLabel("Destination:    ", JLabel.CENTER);
        oriLabel.setFont(new Font("Poppins",Font.BOLD,20));
        destLabel.setFont(new Font("Poppins",Font.BOLD,20));

        //set term panel
        oriPanel.setLayout(new FlowLayout());
        oriPanel.add(oriLabel);
        ori = new JTextField();
        ori.setPreferredSize(new Dimension(400,20));
        oriPanel.add(ori);
        inputPanel.add(oriPanel);

        //set URL panel
        destPanel.setLayout(new FlowLayout());
        destPanel.add(destLabel);
        dest = new JTextField();
        dest.setPreferredSize(new Dimension(400,20));
        destPanel.add(dest);
        inputPanel.add(destPanel);

        //set topPanel Layout
        topPanel.setLayout(new FlowLayout());
        topPanel.add(inputPanel);
        topPanel.add(goButton);

        // set Frame layout
        mainFrame.add(textPanel,BorderLayout.CENTER);
        mainFrame.add(topPanel,BorderLayout.NORTH);

        mainFrame.setVisible(true);
    }

    public void scrapeDestToTextArea(String pDest,String pOri){
        try{
            String URL = "https://skyscanner44.p.rapidapi.com/fly-to-country?destination=" + pDest + "&origin=" + pOri + "&departureDate=2023-07-01&returnDate=2023-07-21&currency=EUR&locale=en-GB&country=UK";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("content-type", "application/octet-stream")
                    .header("X-RapidAPI-Key", "5a0e8c94a4msh655d4f3fd30e085p165896jsn18e640f318ea")
                    .header("X-RapidAPI-Host", "skyscanner44.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            JSONObject obj = new JSONObject(response);
            System.out.println(obj);
            JSONArray arr = obj.getJSONArray("PlacePrices");
            System.out.println(arr);
            String location = arr.getJSONObject(1).getString("Name");
            System.out.println(location);

            //ta.append(link + "\n");
        }catch(Exception ex){
            ta.setText(ex + "");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("GO")) {
                ta.setText("");
                String pDest = dest.getText();
                String pOri = ori.getText();
                scrapeDestToTextArea(pDest,pOri);
            }
        }
    }
}