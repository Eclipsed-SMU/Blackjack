import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;


public class Blackjack {

    private class Card{

        String value;
        String type;

        public String toString(){
            return value + "-" + type;
        }

        Card(String value,String type){
            this.value=value;
            this.type=type;
        }

        public int getvalue() {
            if ("AJQK".contains(value)){ // For A J Q K
                if(value   == "A"){
                    return 11; // For Ace
                }
                return 10;
            }
            
            return Integer.parseInt(value); // returns numers 2- 10 
        }

        public boolean isAce() {
           return value== "A";
        }

        public String getImagePath(){
            return "./cards/"+ toString() + ".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random(); //shuffle deck

    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //Player
    
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //Window
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardHeight = 154;
    int cardWidth = 110;

    JFrame frame = new JFrame("BlackJack Eclipsed");
    JPanel gamePanel = new JPanel(){
    @Override
    public void paintComponent(Graphics g){
            super.paintComponent(g);
            try{
                //draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if(!stayButton.isEnabled()){
                    
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                //draw dealer cards
                for(int i = 0; i<dealerHand.size();i++){
                Card card = dealerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20,cardWidth, cardHeight, null);
                }
                //draw playerer cards
                for (int j = 0; j < playerHand.size(); j++) {
                    Card card = playerHand.get(j);
                    Image  cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                     g.drawImage(cardImg, 20 + (cardWidth + 5)*j, 350, cardWidth, cardHeight, null);
                    
                }

                if(!stayButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("Stay: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String x = "Dealer Hand: " + dealerSum ;
                    String y = "Player Hand: " + playerSum;
                    String message = "";
                    if(playerSum>21){
                        message = "You lose!";
                    }
                    else 
                    if(dealerSum>21){
                        message = "You Win!";
                    }else 
                    if(playerSum==dealerSum){
                        message = "Draw/Tie";
                    }else 
                    if(playerSum<dealerSum){
                        message = "You lose!";
                    }else 
                    if(playerSum>dealerSum){
                        message = "You Win!";
                    }
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Ariel",Font.PLAIN, 30));
                    g.drawString(message, 220, 230);
                    g.drawString(x, 220, 270);
                    g.drawString(y, 220, 320);
                }               
            

            } catch(Exception e){
                e.printStackTrace();
            }
    }
};

    JPanel buttonPanel = new JPanel();
    JButton hitButton  = new JButton("Hit me");
    JButton stayButton  = new JButton("Stay");
    JButton newGame  = new JButton("New Game");

    Blackjack() {
  
        startGame();

        frame.setVisible(true);
        frame.setResizable(true);
        frame.setLocationRelativeTo(frame);
        frame.setSize(boardWidth, boardHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setBackground(new Color (53,101,73));
        gamePanel.setLayout(new BorderLayout());
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        newGame.setFocusable(false);
        newGame.setEnabled(false); 
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(newGame);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
              
                startGame();
                gamePanel.repaint();
                
            }
        });

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                Card card = deck.remove(deck.size()-1);
                playerSum += card.getvalue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);

                if(reducePlayerAce()>21){
                    hitButton.setEnabled(false);
                }

                gamePanel.repaint();
            }

            
        });

        stayButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e){
            hitButton.setEnabled(false);
            stayButton.setEnabled(false);
            newGame.setEnabled(true); 

             
            while(dealerSum<17){
                Card card = deck.remove(deck.size()-1);
                dealerHand.add(card);
                dealerSum += card.getvalue();
                dealerAceCount += card.isAce()? 1:0;
                            }

                gamePanel.repaint();
           
           gamePanel.repaint();}
        });


        gamePanel.repaint();   

    }

    public void startGame() {

        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
             hitButton.setEnabled(true);
             stayButton.setEnabled(true); 
             newGame.setEnabled(false); 
             gamePanel.repaint();
            } 
         });
            //deck
            buildDeck();
            shuffleDeck();

            //Dealer
            dealerHand = new ArrayList<>();
            dealerSum = 0;
            dealerAceCount = 0;

            hiddenCard = deck.remove(deck.size()-1); //removes card at last index
            dealerSum += hiddenCard.getvalue();
            dealerAceCount += hiddenCard.isAce() ? 1:0;

            Card card = deck.remove(deck.size()-1);
            dealerSum +=card.getvalue();
            dealerAceCount += card.isAce() ? 1 : 0;
            dealerHand.add(card);

            System.out.println("Dealer");
            System.out.println(hiddenCard);
            System.out.println(dealerHand);
            System.out.println(dealerSum);
            System.out.println(dealerAceCount);

            //Player
            playerHand = new ArrayList<>();
            playerSum= 0;
            playerAceCount = 0;

            

            Card card2 = deck.remove(deck.size()-1);
            Card card3 = deck.remove(deck.size()-1);

            playerHand.add(card3);
            playerHand.add(card2);

            playerAceCount += card3.isAce() ? 1 : 0; 
            playerAceCount += card2.isAce() ? 1 : 0;

            playerSum += card2.getvalue();
            playerSum += card3.getvalue();
            

            System.out.println("Player");
            //System.out.println(hiddenCard);
            System.out.println(playerHand);
            System.out.println(playerSum);
            System.out.println(playerAceCount);
            


    }

    public void shuffleDeck() {

        /*for(int i=0; i<deck.size();i++){
            int j = random.nextInt(deck.size());
            Card currentCard= deck.get(i);
            Card randomCard= deck.get(j);

            deck.set(i, randomCard);
            deck.set(j, currentCard);
        }*/
        Collections.shuffle(deck);
        System.out.println("New Deck:");
        System.out.println(deck);
        
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values= {"A","2","3","4","5","6","7","8","9","J","Q","K"};
        String[] types= {"C","H","D","S",};

        for(int i=0; i<types.length;i++){
            for(int j=0; j<values.length;j++){
                Card card = new Card(values[j],types[i]);
                deck.add(card);
            }
        }

        System.out.println("Build deck:");
        System.out.println(deck);
    }
    
    public int reducePlayerAce() {
        while(playerSum>21 && playerAceCount>0){
            playerAceCount-=1;
            playerSum -=10;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while(dealerSum>21 && dealerAceCount>0){
            dealerAceCount-=1;
            dealerSum -=10;
        }
        return dealerSum;
    }
}
