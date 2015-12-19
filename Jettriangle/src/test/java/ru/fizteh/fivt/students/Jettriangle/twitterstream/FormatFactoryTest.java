package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 14.10.15.
 */
import com.beust.jcommander.JCommander;
import org.junit.Test;
import twitter4j.Status;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import twitter4j.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import static org.junit.Assert.*;

public class FormatFactoryTest {

    static final String SEPARATOR =
            "\n----------------------------------------------------------------------------------------\n";;

    @Test
    public void testForrmatRetweets() {

        User user1 = mock(User.class);
        when(user1.getName()).thenReturn("user1");

        User user2 = mock(User.class);
        when(user2.getName()).thenReturn("user2");

        Status statusRetweeted = mock(Status.class);

        String [] argv = {""};
        JCommanderTwitter jct = new JCommanderTwitter();
        JCommander jcparser = new JCommander(jct, argv);

        when(statusRetweeted.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        when(statusRetweeted.isRetweet()).thenReturn(false);
        when(statusRetweeted.getUser()).thenReturn(user2);
        when(statusRetweeted.getText()).thenReturn("Hello.");
        when(statusRetweeted.isRetweeted()).thenReturn(true);
        when(statusRetweeted.getRetweetCount()).thenReturn(10);

        assertEquals("[только что] @user2: Hello. (10 ретвитов)" + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweeted, jct));
        when(statusRetweeted.getRetweetCount()).thenReturn(2);
        assertEquals("[только что] @user2: Hello. (2 ретвита)" + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweeted, jct));
        when(statusRetweeted.getRetweetCount()).thenReturn(21);
        assertEquals("[только что] @user2: Hello. (21 ретвит)" + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweeted, jct));
        when(statusRetweeted.getRetweetCount()).thenReturn(117);
        assertEquals("[только что] @user2: Hello. (117 ретвитов)" + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweeted, jct));
        when(statusRetweeted.getRetweetCount()).thenReturn(204);
        assertEquals("[только что] @user2: Hello. (204 ретвита)" + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweeted, jct));
        Status statusRetweet = mock(Status.class);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        when(statusRetweet.isRetweet()).thenReturn(true);
        when(statusRetweet.getUser()).thenReturn(user1);
        when(statusRetweet.getRetweetedStatus()).thenReturn(statusRetweeted);
        assertEquals("[только что] @user1 ретвитнул @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));

    }

    @Test
    public void testFormatNoRetweets() {

        String [] argv = {"--hideRetweets"};
        JCommanderTwitter jct = new JCommanderTwitter();
        JCommander jcparser = new JCommander(jct, argv);

        User marlonBrando = mock(User.class);
        when(marlonBrando.getName()).thenReturn("user2");

        Status status = mock(Status.class);
        when(status.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        when(status.isRetweet()).thenReturn(false);
        when(status.getUser()).thenReturn(marlonBrando);
        when(status.getText()).thenReturn("Hello.");
        when(status.isRetweet()).thenReturn(false);
        assertEquals("[только что] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(status, jct));
    }

    @Test
    public void testTimeForms() {

        String [] argv = {"--hideRetweets"};
        JCommanderTwitter jct = new JCommanderTwitter();
        JCommander jcparser = new JCommander(jct, argv);

        User marlonBrando = mock(User.class);
        when(marlonBrando.getName()).thenReturn("user2");

        Status statusRetweet = mock(Status.class);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime newTime;


        when(statusRetweet.isRetweet()).thenReturn(false);
        when(statusRetweet.getUser()).thenReturn(marlonBrando);
        when(statusRetweet.getText()).thenReturn("Hello.");
        when(statusRetweet.isRetweet()).thenReturn(false);

        newTime = currentTime.minusHours(3);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            assertEquals("[3 часа назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        } else {
            assertEquals("[вчера] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        }


        newTime = currentTime.minusHours(1);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            assertEquals("[1 час назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        } else {
            assertEquals("[вчера] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        }


        newTime = currentTime.minusHours(10);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            assertEquals("[10 часов назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        } else {
            assertEquals("[вчера] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        }

        newTime = currentTime.minusDays(1);
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            newTime = currentTime.minusDays(2);
        }
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[вчера] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));

        newTime = currentTime.minusDays(2);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[2 дня назад] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));
        newTime = currentTime.minusDays(5);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[5 дней назад] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));
        newTime = currentTime.minusDays(31);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[31 день назад] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));
        newTime = currentTime.minusDays(1543);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[1543 дня назад] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));
        newTime = currentTime.minusDays(111);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[111 дней назад] @user2: Hello." + SEPARATOR,
                FormatFactory.getTweetFormat(statusRetweet, jct));

        newTime = currentTime.minusMinutes(3);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.HOURS.between(newTime, currentTime) == 0) {
            assertEquals("[3 минуты назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        } else {
            assertEquals("[1 час назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        }

        newTime = currentTime.minusMinutes(21);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.HOURS.between(newTime, currentTime) == 0) {
            assertEquals("[21 минуту назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        } else {
            assertEquals("[1 час назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        }

        newTime = currentTime.minusMinutes(5);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.HOURS.between(newTime, currentTime) == 0) {
            assertEquals("[5 минут назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        } else {
            assertEquals("[1 час назад] @user2: Hello." + SEPARATOR,
                    FormatFactory.getTweetFormat(statusRetweet, jct));
        }


    }
}
