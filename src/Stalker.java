import twitter4j.*;

import java.util.*;

public class Stalker {
  public static void main(String[] args) throws TwitterException, InterruptedException {
    TwitterFactory tf = new TwitterFactory();
    Twitter twitter = tf.getInstance();

    Scanner sc = new Scanner(System.in);

    while (true) {
      System.out.println("Enter name of person:");
      String searchName = "";
      while (searchName.length() == 0) {
        searchName = sc.nextLine();
      }
      System.out.println("[Searching... " + new Date());

      ResponseList<User> searchResults = twitter.searchUsers(searchName, -1);
      for (int i = 0; i < searchResults.size(); i++) {
        System.out.println(i + "." + searchResults.get(i).getName() + "  " +
            searchResults.get(i).getScreenName());
        System.out.println(searchResults.get(i).getBiggerProfileImageURL());
        System.out.println();
      }
      System.out.println("Enter -1 if you want to search again!");
      System.out.println();
      System.out.println("Input ->");
      int choice = sc.nextInt();
      if (choice == -1) {
        continue;
      } else if (choice < 0 || choice >= searchResults.size()) {
        endCredits();
        return;
      }

      User user = searchResults.get(choice);

      HashMap<Long, Integer> mapLikes = new HashMap<Long, Integer>();
      HashMap<Long, String> mapName = new HashMap<Long, String>();
      int pageCounter = 1;
      int totalFavourites = 0;
      while (true) {
        Paging paging = new Paging();
        paging.setCount(user.getFavouritesCount());
        ResponseList<Status> favourites;
        try {
          favourites = twitter.getFavorites(user.getId(), new Paging(pageCounter, 1000));
        } catch (TwitterException te) {
          System.out.println("Twitter exception caught for exceeding Rate " +
              "Limit!!");
          Date date = new Date();
          int timeRemaining = te.getRateLimitStatus().getSecondsUntilReset();
          long timeStop = date.getTime() + timeRemaining * 1000;
          System.out.println("Would you like to wait for " + timeRemaining /
              (double) 60
              + " minutes? (Y/N)");
          String resopnse = "";
          while (resopnse.length() == 0) {
            resopnse = sc.nextLine();
          }
          if (resopnse.equalsIgnoreCase("N")) {
            endCredits();
            return;
          }

          System.out.println("[Started waiting... " + date);
          while (new Date().getTime() < timeStop) {
          }
          System.out.println("[...stopped waiting " + new Date());
          continue;
        }
        pageCounter++;

        totalFavourites += favourites.size();
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
      System.out.println("total likes: " + totalFavourites);
      mapLikes = sortHashMapByValues(mapLikes);
      Set<Long> userIds = mapLikes.keySet();
      for (long userId : userIds) {
        int likes = mapLikes.get(userId);
        String name = mapName.get(userId);
        System.out.println(name + " " + likes);
      }
      System.out.println();
      System.out.print("Would you like to continue (Y or N): ");
      String c = sc.next();
      if (c.equalsIgnoreCase("n")) {
        endCredits();
        break;
      }

    }
  }

  private static void endCredits() {
    System.out.println("-----------------------------------------------------");
    System.out.println("Thank you!");
    System.out.println("Credits: ");
    System.out.println("Rishabh Jain");
    System.out.println("wakeuprj@gmail.com");
    System.out.println("-----------------------------------------------------");
  }

  public static HashMap<Long, Integer> sortHashMapByValues(
      HashMap<Long, Integer> passedMap) {
    List<Long> mapKeys = new ArrayList<Long>(passedMap.keySet());
    List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
    Collections.sort(mapValues);
    Collections.sort(mapKeys);

    HashMap<Long, Integer> sortedMap =
        new LinkedHashMap<Long, Integer>();

    for (Integer val : mapValues) {
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
