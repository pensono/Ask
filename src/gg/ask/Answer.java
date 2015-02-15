package gg.ask;

import java.util.Arrays;

public class Answer {

    private String begin;
    private String keyword;
    private String[] related;
    private String answer;

    public Answer( String keyword, String begin, String[] related, String answer) {
        this.begin = begin;
        this.keyword = keyword;
        this.related = related;
        this.answer = answer;
    }

    public Answer(String answer) {
        this.begin = "Begin";
        this.keyword = "Keyword";
        this.related = null;
        this.answer = answer;
    }
    
    public static Answer parseAnswer(String line){
        String[] split = line.split("/");//Split on the /
        return new Answer(split[0], split[1], split[2].split(","), split[3]);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return keyword + "/" + begin + "/"+ Arrays.toString(related) + "/" + answer;
    }

    public String getBegin() {
        return begin;
    }

    public String getKeyword() {
        return keyword;
    }

    public String[] getRelated() {
        return related;
    }

    public String getAnswer() {
        return answer;
    }
}
