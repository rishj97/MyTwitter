import twitter4j.*;

import java.util.*;

public class TwitterLikes {
  final static int MILLI_OFFSET = 1000;
  final static int SECS_IN_MIN = 60;
  final static String ANSI_CLS = "\033[2J\033[1;1H";

  public static void main(String[] args) throws TwitterException, InterruptedException {
    TwitterFactory tf = new TwitterFactory();
    Twitter twitter = tf.getInstance();

    Scanner sc = new Scanner(System.in);

    while (true) {
      System.out.print(ANSI_CLS);
      System.out.println("Enter name of person:");
      String searchName = "";
      while (searchName.length() == 0) {
        searchName = sc.nextLine();
      }
      System.out.print(ANSI_CLS);
      System.out.println("[Searching... " + new Date());

      ResponseList<User> searchResults = twitter.searchUsers(searchName, -1);
      System.out.print(ANSI_CLS);
      for (int i = 0; i < Math.min(searchResults.size(), 5); i++) {
        System.out.println(i + "." + searchResults.get(i).getName() + "  " +
            searchResults.get(i).getScreenName());
        System.out.println(searchResults.get(i).getLocation());
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
        ResponseList<Status> favourites;
        try {
          favourites = twitter.getFavorites(user.getId(), new Paging(pageCounter, 1000));
        } catch (TwitterException te) {
          System.out.println("Twitter exception caught for exceeding Rate " +
              "Limit!!");
          Date date = new Date();
          int timeRemaining = te.getRateLimitStatus().getSecondsUntilReset();
          long timeStop = date.getTime() + timeRemaining * MILLI_OFFSET;

          System.out.println("Would you like to wait for " + timeRemaining /
              (double) SECS_IN_MIN
              + " minutes? (y or n)");
          String resopnse = "";
          while (resopnse.length() == 0) {
            resopnse = sc.nextLine();
          }
          if (resopnse.equalsIgnoreCase("n")) {
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
      System.out.print(ANSI_CLS);
      System.out.println("total likes: " + totalFavourites);
      mapLikes = sortHashMapByValues(mapLikes);
      Set<Long> userIds = mapLikes.keySet();
      for (long userId : userIds) {
        int likes = mapLikes.get(userId);
        String name = mapName.get(userId);
        System.out.println(name + " " + likes);
      }
      System.out.println();
      System.out.print("Would you like to continue (y or n): ");
      String c = sc.next();
      if (c.equalsIgnoreCase("n")) {
        endCredits();
        break;
      }

    }
  }

  private static void endCredits() {
    System.out.print(ANSI_CLS);
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
