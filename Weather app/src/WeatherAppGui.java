import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame{
    private JSONObject weatherData;
    public WeatherAppGui(){
        super("The Weather");

        //setting config to exit on close
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //set sizing (Pixel, Pixel)
        setSize(450,650);

        //setting to open in middle of screen
        setLocationRelativeTo(null);

        //layout manager to be null
        setLayout(null);

        //resize our GUI
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents(){
        JTextField searchField = new JTextField();

        //set location and size of text field
        searchField.setBounds(15,15,351,48);

        //changing fonts
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchField);

        //weather image
        JLabel weatherCond = new JLabel(loadImage("src/images/cloudy.png"));
        weatherCond.setBounds(0,125,450,217);
        add(weatherCond);

        //Temperature text
        JLabel tempText = new JLabel("10 F");
        tempText.setBounds(0,350,450,54);
        tempText.setFont(new Font("Dialog", Font.BOLD, 48));

        //Center Text
        tempText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tempText);

        //Weather Condition
        JLabel weatherConditionDes = new JLabel("Cloudy");
        weatherConditionDes.setBounds(0,405,450,36);
        weatherConditionDes.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDes.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDes);

        //humidity image
        JLabel humidityImage = new JLabel(loadImage("src/images/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        //humidity text
        JLabel humidText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidText.setBounds(90,500,85,55);
        humidText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidText);


        //WindSpeed
        JLabel windSpeed = new JLabel(loadImage("src/images/windspeed.png"));
        windSpeed.setBounds(220,500, 74, 66);
        add(windSpeed);

        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 9mph</html>");
        windSpeedText.setBounds(310,500,85,55);
        windSpeedText.setFont(new Font("Dialog",Font.PLAIN, 16));
        add(windSpeedText);



        JButton searchButton = new JButton(loadImage("src/images/search.png"));

        //changing cursor to hand
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchField.getText();

                if(userInput.replace("\\s", "").length() <= 0){
                    return;
                }

                //getting weather data from backend
                weatherData = WeatherAppAPI.getWeatherData(userInput);

                String weatherCon = (String) weatherData.get("weather_condition");

                switch(weatherCon){
                    case "Clear":
                        weatherCond.setIcon(loadImage("src/images/clear.png"));
                        break;
                    case "Cloudy":
                        weatherCond.setIcon(loadImage("src/images/cloudy.png"));
                        break;
                    case "Rain":
                        weatherCond.setIcon(loadImage("src/images/rain.png"));
                        break;
                    case "Snow":
                        weatherCond.setIcon(loadImage("src/images/snow.png"));
                        break;
                }

                //updating temperature text
                double temperature = (double) weatherData.get("temperature");
                tempText.setText(temperature + " F");

                //update weather condition text
                weatherConditionDes.setText(weatherCon);

                //updating humidity
                long humidity = (long) weatherData.get("humidity");
                humidText.setText("<html><b>Humidty</b> " + humidity + "%</html>");

                //update windspeed
                double theWindSpeed = (double) weatherData.get("wind-speed");
                windSpeed.setText("<html><b>Windspeed</b> " + theWindSpeed + " mph</html>" );

            }
        });
        add(searchButton);








    }

    private ImageIcon loadImage(String imagePath){
        try{
            BufferedImage image = ImageIO.read(new File(imagePath));


            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Resource doesn't exist");
        return null;
    }

}