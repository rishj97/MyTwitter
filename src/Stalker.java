import twitter4j.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by rishabh on 09/12/16.
 */
public class Stalker {
  public static void main(String[] args) throws TwitterException {
    TwitterFactory tf = new TwitterFactory();
    Twitter twitter = tf.getInstance();

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter name of person you want to stalk lol:");
    String stalk = sc.nextLine();
    ResponseList<User> searchResults = twitter.searchUsers(stalk, -1);
    for (int i = 0; i < searchResults.size(); i++) {
      System.out.println(i + ".");
      System.out.println(searchResults.get(i).getName());
      System.out.println(searchResults.get(i).getBiggerProfileImageURL());
      System.out.println();
    }
    System.out.println("Choose");
    int choice = sc.nextInt();
    assert (choice >= 0 && choice < searchResults.size()) : "Invalid index";

    User userStalk = searchResults.get(choice);

//    FriendsFollowersResources fnfResourses = twitter.friendsFollowers();
    HashMap<Long, Integer> mapLikes = new HashMap<Long, Integer>();
    HashMap<Long, String> mapName = new HashMap<Long, String>();
    int flag = 1;
    int totalLikes = 0;
    while (flag != 0) {
      Paging paging = new Paging();
      paging.setCount(userStalk.getFavouritesCount());
      ResponseList<Status> favourites = twitter.getFavorites(userStalk.getId(),
          new Paging(flag++, 1000));
      totalLikes += favourites.size();
      if(favourites.size() == 0) {
        flag = 0;
      }
      for (Status status : favourites) {

        if (mapLikes.get(status.getUser().getId()) == null) {
          mapLikes.put(status.getUser().getId(), 1);
          mapName.put(status.getUser().getId(), status.getUser().getName());
        } else {
          int n = mapLikes.get(status.getUser().getId());
          mapLikes.put(status.getUser().getId(), n + 1);
        }
      }
    }
    System.out.println("total likes: " + totalLikes);
    Set<Long> userIds = mapLikes.keySet();
    for (long userId : userIds) {
      int likes = mapLikes.get(userId);
      String name = mapName.get(userId);
      System.out.println(name +  '\t' + '\t' + '\t' + likes);
    }
  }
}
