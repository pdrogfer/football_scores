package barqsoft.footballscores;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
    // be updated. Feel free to use the codes
    public static final int BUNDESLIGA_1 = 394;
    public static final int BUNDESLIGA_2 = 395;
    public static final int BUNDESLIGA_3 = 403;
    public static final int LIGUE_1 = 396;
    public static final int LIGUE_2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMEIRA_LIGA = 402;
    public static final int EREDIVISIE = 404;
    public static final int CHAMPIONS_LEAGUE = 405;
    public static final int LEAGUE_ONE = 425;
    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case BUNDESLIGA_1 : return "Bundesliga 1";
            case BUNDESLIGA_2 : return "Bundesliga 2";
            case BUNDESLIGA_3 : return "Bundesliga 3";
            case LIGUE_1: return "Ligue 1";
            case LIGUE_2: return "Ligue 2";
            case PREMIER_LEAGUE: return "Premier League";
            case PRIMERA_DIVISION: return "Primera Division";
            case SEGUNDA_DIVISION: return "Segunda Division";
            case SERIE_A: return "Serie A";
            case PRIMEIRA_LIGA: return "Primeira Liga";
            case EREDIVISIE: return "Eredivisie";
            case CHAMPIONS_LEAGUE: return "Champions League";
            case LEAGUE_ONE: return "League One";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.

            // premier league
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;

            // primera division

            // segunda division
            default: return R.drawable.no_icon;
        }
    }
}
