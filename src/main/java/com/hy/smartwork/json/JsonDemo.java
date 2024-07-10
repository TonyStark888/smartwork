package com.hy.smartwork.json;

public class JsonDemo {
    public static void main(String[] args) {
        String str = "Generous angpao from my beautiful Mom is always expected\n" +
                "May I have an angpao from a gretest Dad in the world, please?\n" +
                "I'm still waiting for that angpao from the best brother in the family!\n" +
                "To my dearest sister that never forget to send me some angpao ;)\n" +
                "I know it's my birthday, but angpao from is always be a special treat\n" +
                "Never too much of good angpao from you!\n" +
                "Last night I dreamt about someone sending me angpao. Will it be you?\n" +
                "There's always a room for special angpao from someone special like you\n" +
                "Didn't you promise to send me angpao? I wonder where is it now\n" +
                "Angpao always make my day. Would you like to make it for me, please?\n" +
                "A perfect day consists good music, good coffee, and angpao from you!\n" +
                "Today is the day! Yes, today you're going to send me the angpao :))\n" +
                "Don't need to wait until tomorrow if you want to send the angpao ;)\n" +
                "You need no permission to send me the angpao. Just send it now, haha!\n" +
                "What a lovely day if you would care to send me Neo Angpao\n" +
                "Forget to buy the gift for my birthday? Neo Angpao will do just fine\n" +
                "An angpao a day keeps the jealousy away";

        String[] arrays = str.split("\n");

        System.out.println(arrays.length);
        StringBuffer sb = new StringBuffer();
        for (String s: arrays) {
            sb.append("{\"greet\": \"");
            sb.append(s);
            sb.append("\"},");
        }
        System.out.println(sb.toString());
    }
}
