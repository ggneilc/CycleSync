package cyclesync.Rentals;

public class RentalReviewRequest {
    private int rating;
    private String review;

    // You could also add validation annotations here, e.g @NotNull, @Min(1), etc

    // Getters and setters
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
