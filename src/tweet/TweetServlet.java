package tweet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jakubinyi on 2017.04.05..
 */
public class TweetServlet extends HttpServlet {

    private List<Tweet> tweets = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String content = request.getParameter("content");
        String poster = request.getParameter("poster");

        if(poster.equals("") || content.equals("")) {
            response.setContentType("text/html");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Error page</title>\n" +
                    "    <link type=\"text/css\" rel=\"stylesheet\" href=\"style.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div class=\"index\">\n" +
                            "<h4>\"content\": \"Do. Or do not. There is no try.\",</h4>\n" +
                            "<h4>\"poster\": \"Yoda\"</h4>\n" +
                            "</div>\n" +
                    "</body>\n" +
                    "</html>");
        } else {
            tweets.add(0,new Tweet(poster,content));
            tweets.get(tweets.size()-1).setId(tweets.size()-1);
            //dateFormat.format(tweets.get(tweets.size()-1));
            RequestDispatcher index = request.getRequestDispatcher("/index.html");
            index.forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String limit = request.getParameter("limit");
        String offset = request.getParameter("offset");
        String poster = request.getParameter("poster");
        String from = request.getParameter("from");

        int limitt;
        int offsett;
        Date fromm;

        if(limit.equals("")) {
            limitt = 10;
        } else {
            try {
                limitt = Integer.valueOf(limit).intValue();
            } catch (NumberFormatException e) {
                response.sendError(400, "Wrong limit format " + limit);
                return;
            }
        }

        if(offset.equals("")) {
            offsett = 0;
        } else {
            try {
                offsett = Integer.valueOf(offset).intValue();
            } catch (NumberFormatException e) {
                response.sendError(400, "Wrong offset format " + offset);
                return;
            }
        }

        if(from.equals("")) {
            fromm = null;
        } else {
            try {
                fromm = dateFormat.parse(from);
            } catch (ParseException e) {
                response.sendError(400, "Wrong date format " + from);
                return;
            }
        }

        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();

        printWriter.println(
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Requested messages</title>\n" +
                "    <link type=\"text/css\" rel=\"stylesheet\" href=\"style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                        "<div class=\"index\">\n" +
                        "<h4>Requested messages: </h4>\n" +
                        "</div>\n" +
                "</body>\n" +
                "</html>");

        /*for (Tweet tweet : filteredTweets) {
            printWriter.println("<br><div class=\"index\">\n");
            printWriter.println("<h4>Tweet: " + tweet.getContent() + "</h4>");
            printWriter.println("<h5>Poster: " + tweet.getPoster() + "</h5>");
            printWriter.println("<h6>Time: " + dateFormat.format(tweet.getDate()) + "</h6>");
            printWriter.println("</div>\n");
        }*/

        tweets = filterTweets(limitt, offsett, poster, fromm);

        for(Tweet tweet : tweets) {
            printWriter.println("<br><div class=\"index\">\n");
            printWriter.println("<h4> " + tweet.getPoster() +" <" + dateFormat.format(tweet.getDate()) + ">: "+ tweet.getContent() + "</h4>");
            printWriter.println("</div>\n");
        }

    }

    private ArrayList<Tweet> filterTweets(int limit,int offset, String name, Date from) {
        /*if (nameFilter.equals("")) {
            filteredTweets = new ArrayList<>(tweets);
        }*/
        ArrayList<Tweet> filteredTweets = new ArrayList<>();

        for (int i = offset; i< tweets.size() && filteredTweets.size() < limit; i++){
            Tweet tweet = tweets.get(i);
            if ((name.equals("") || tweet.getPoster().equals(name))
                    && (from == null || tweet.getDate().after(from))) {
                filteredTweets.add(tweet);
            }
        }
        return filteredTweets;
    }

}
