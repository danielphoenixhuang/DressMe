package da.dressme;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ColorAnalysisClass {

    public static String colorMatch(String topColor1, String topColor2, String bottomColor1, String bottomColor2)
    {

        ArrayList<String> pinkRedComp = new ArrayList<>();
        pinkRedComp.add("light blue");
        pinkRedComp.add("dark blue");
        pinkRedComp.add("grey");
        pinkRedComp.add("white");
        pinkRedComp.add("black");

        ArrayList<String> pinkTone = new ArrayList<>();
        pinkTone.add("red");
        pinkTone.add("beige");

        ArrayList<String> redTone = new ArrayList<>();
        redTone.add("pink");
        redTone.add("beige");

        ArrayList<String> orangeComp = new ArrayList<>();
        orangeComp.add("light blue");
        orangeComp.add("dark blue");
        orangeComp.add("green");
        orangeComp.add("white");
        orangeComp.add("black");

        ArrayList<String> orangeTone = new ArrayList<>();
        orangeTone.add("beige");
        orangeTone.add("brown");

        ArrayList<String> beigeComp = new ArrayList<>();
        beigeComp.add("purple");
        beigeComp.add("dark blue");
        beigeComp.add("brown");
        beigeComp.add("white");
        beigeComp.add("black");

        ArrayList<String> beigeTone = new ArrayList<>();
        beigeTone.add("yellow");
        beigeTone.add("orange");

        ArrayList<String> yellowComp = new ArrayList<>();
        yellowComp.add("green");
        yellowComp.add("dark blue");
        yellowComp.add("white");
        yellowComp.add("black");

        ArrayList<String> yellowTone = new ArrayList<>();
        yellowTone.add("beige");

        ArrayList<String> greenComp = new ArrayList<>();
        greenComp.add("purple");
        greenComp.add("orange");
        greenComp.add("white");
        greenComp.add("black");

        ArrayList<String> greenTone = new ArrayList<>();
        greenTone.add("yellow");
        greenTone.add("light blue");

        ArrayList<String> lightBlueComp = new ArrayList<>();
        lightBlueComp.add("pink");
        lightBlueComp.add("red");
        lightBlueComp.add("orange");
        lightBlueComp.add("white");
        lightBlueComp.add("black");

        ArrayList<String> lightBlueTone = new ArrayList<>();
        lightBlueTone.add("dark blue");
        lightBlueTone.add("purple");

        ArrayList<String> darkBlueComp = new ArrayList<>();
        darkBlueComp.add("pink");
        darkBlueComp.add("red");
        darkBlueComp.add("yellow");
        darkBlueComp.add("grey");
        darkBlueComp.add("white");
        darkBlueComp.add("black");

        ArrayList<String> darkBlueTone = new ArrayList<>();
        darkBlueTone.add("light blue");
        darkBlueTone.add("purple");

        ArrayList<String> purpleComp = new ArrayList<>();
        purpleComp.add("orange");
        purpleComp.add("grey");
        purpleComp.add("green");
        purpleComp.add("white");
        purpleComp.add("black");

        ArrayList<String> purpleTone = new ArrayList<>();
        purpleTone.add("light blue");
        purpleTone.add("dark blue");

        ArrayList<String> brownComp = new ArrayList<>();
        brownComp.add("beige");
        brownComp.add("white");
        brownComp.add("black");

        ArrayList<String> brownTone = new ArrayList<>();
        brownTone.add("orange");

        ArrayList<String> greyComp = new ArrayList<>();
        greyComp.add("pink");
        greyComp.add("red");
        greyComp.add("dark blue");
        greyComp.add("purple");

        ArrayList<String> greyTone = new ArrayList<>();
        greyTone.add("white");
        greyTone.add("black");

        switch(topColor1)
        {
            case "pink":
                if(pinkRedComp.contains(topColor2) && pinkRedComp.contains(bottomColor1) && pinkRedComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(pinkTone.contains(topColor2) && pinkTone.contains(bottomColor1) && pinkTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "red":
                if(pinkRedComp.contains(topColor2) && pinkRedComp.contains(bottomColor1) && pinkRedComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(redTone.contains(topColor2) && redTone.contains(bottomColor1) && redTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "orange":
                if(orangeComp.contains(topColor2) && orangeComp.contains(bottomColor1) && orangeComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(orangeTone.contains(topColor2) && orangeTone.contains(bottomColor1) && orangeTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "beige":
                if(beigeComp.contains(topColor2) && beigeComp.contains(bottomColor1) && beigeComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(beigeTone.contains(topColor2) && beigeTone.contains(bottomColor1) && beigeTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "yellow":
                if(yellowComp.contains(topColor2) && yellowComp.contains(bottomColor1) && yellowComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(yellowTone.contains(topColor2) && yellowTone.contains(bottomColor1) && yellowTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "green":
                if(greenComp.contains(topColor2) && greenComp.contains(bottomColor1) && greenComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(greenTone.contains(topColor2) && greenTone.contains(bottomColor1) && greenTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "light blue":
                if(lightBlueComp.contains(topColor2) && lightBlueComp.contains(bottomColor1) && lightBlueComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(lightBlueTone.contains(topColor2) && lightBlueTone.contains(bottomColor1) && lightBlueTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "dark blue":
                if(darkBlueComp.contains(topColor2) && darkBlueComp.contains(bottomColor1) && darkBlueComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(darkBlueTone.contains(topColor2) && darkBlueTone.contains(bottomColor1) && darkBlueTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "purple":
                if(purpleComp.contains(topColor2) && purpleComp.contains(bottomColor1) && purpleComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(purpleTone.contains(topColor2) && purpleTone.contains(bottomColor1) && purpleTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "brown":
                if(brownComp.contains(topColor2) && brownComp.contains(bottomColor1) && brownComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(brownTone.contains(topColor2) && brownTone.contains(bottomColor1) && brownTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "grey":
                if(greyComp.contains(topColor2) && greyComp.contains(bottomColor1) && greyComp.contains(bottomColor2))
                    return "matching - complementary";
                else if(greyTone.contains(topColor2) && greyTone.contains(bottomColor1) && greyTone.contains(bottomColor2))
                    return "matching - tonal";
                else
                    return "colors don't match, consider changing your pants!";

            case "black":
                return "Black goes well with anything!";

            case "white":
                return "White goes well with anything!";

            default:
                return "Matching Error";
        }

    }


}
