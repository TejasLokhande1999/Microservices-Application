package com.example.reviewms.review.impl;


import com.example.reviewms.review.Review;
import com.example.reviewms.review.ReviewRepository;
import com.example.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

//SpringBoot knows this is a service and it should manage it
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;


    //SB will make sure it will auto inject the instance of this on runtime
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        List<Review> reviews =reviewRepository.findByCompanyId(companyId);
        return reviews;
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if(companyId!=null && review!=null){
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(reviewId!=null){  //If we get a company object
            review.setTitle(updatedReview.getTitle());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            review.setCompanyId(updatedReview.getCompanyId());
            reviewRepository.save(updatedReview);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review= reviewRepository.findById(reviewId).orElse(null);
        if(review!=null){ //Company and review present
         reviewRepository.delete(review);
            return true;
        }else{
            return false;
        }
    }
}
