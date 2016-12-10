import twitter4j.*;

import java.util.Scanner;

public class UpdateTwitterStatus {

  public static void main(String[] args) throws TwitterException {

    TwitterFactory tf = new TwitterFactory();
    Twitter twitter = tf.getInstance();
    String status;
    if (args.length == 0) {
      System.out.println("Enter new status");
      status = new Scanner(System.in).nextLine();
    } else {
      status = args[0];
    }
    twitter.updateStatus(status);
    System.out.println();
    System.out.println("Status updated successfully!");
  }
}
