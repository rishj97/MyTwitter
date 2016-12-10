import twitter4j.*;

import java.util.*;

public class Stalker {
  public static void main(String[] args) throws TwitterException, InterruptedException {
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

    HashMap<Long, Integer> mapLikes = new HashMap<Long, Integer>();
    HashMap<Long, String> mapName = new HashMap<Long, String>();
    int flag = 1;
    int totalLikes = 0;
    while (true) {
      Paging paging = new Paging();
      paging.setCount(userStalk.getFavouritesCount());
      ResponseList<Status> favourites = null;
      try {
        favourites = twitter.getFavorites(userStalk.getId(), new Paging(flag, 1000));
      } catch (TwitterException te) {
        System.out.println("twitter exception caught");
        Date date = new Date();
        int timeRemaining = te.getRateLimitStatus().getSecondsUntilReset();
        long timeStop = date.getTime() + timeRemaining * 1000;
        System.out.println("started waiting... " + date);
        while (new Date().getTime() < timeStop) {
        }
        System.out.println("...stopped waiting " + new Date());
        continue;
      }
      flag++;

      totalLikes += favourites.size();
      if (favourites.size() == 0) {
        break;
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
    mapLikes = sortHashMapByValues(mapLikes);
    Set<Long> userIds = mapLikes.keySet();
    for (long userId : userIds) {
      int likes = mapLikes.get(userId);
      String name = mapName.get(userId);
      System.out.println(name + " " + likes);
    }
  }

  public static HashMap<Long, Integer> sortHashMapByValues(
      HashMap<Long, Integer> passedMap) {
    List<Long> mapKeys = new ArrayList<Long>(passedMap.keySet());
    List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
    Collections.sort(mapValues);
    Collections.sort(mapKeys);

    HashMap<Long, Integer> sortedMap =
        new LinkedHashMap<Long, Integer>();

    Iterator<Integer> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
      int val = valueIt.next();
      Iterator<Long> keyIt = mapKeys.iterator();

      while (keyIt.hasNext()) {
        Long key = keyIt.next();
        int comp1 = passedMap.get(key);
        int comp2 = val;

        if (comp1 == comp2) {
          keyIt.remove();
          sortedMap.put(key, val);
          break;
        }
      }
    }
    return sortedMap;
  }

}
