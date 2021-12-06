package edu.northeastern.cs5520.numadfa21_happytravel;

public class User {


    private String name;
    private PostDTO post;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public static class PostDTO {
        private Integer eat;
        private Integer play;
        private Integer post;

        public Integer getEat() {
            return eat;
        }

        public void setEat(Integer eat) {
            this.eat = eat;
        }

        public Integer getPlay() {
            return play;
        }

        public void setPlay(Integer play) {
            this.play = play;
        }

        public Integer getPost() {
            return post;
        }

        public void setPost(Integer post) {
            this.post = post;
        }
    }
}
