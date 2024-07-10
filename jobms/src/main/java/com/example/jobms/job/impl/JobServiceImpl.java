package com.example.jobms.job.impl;


import com.example.jobms.job.Job;
import com.example.jobms.job.JobRepository;
import com.example.jobms.job.JobService;
import com.example.jobms.job.clients.CompanyClient;
import com.example.jobms.job.clients.ReviewClient;
import com.example.jobms.job.dto.JobDTO;
import com.example.jobms.job.external.Company;
import com.example.jobms.job.external.Review;
import com.example.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

 //   private  List<Job> jobs = new ArrayList<>();

    //Defining a repo object
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;


    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    private Long nextId = 1l;


    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient,
                          ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient=companyClient;
        this.reviewClient = reviewClient;
    }

//    @Override
//    public List<Job> findall() {
//        return jobs;
//    }

    @Override
    //@CircuitBreaker(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    //@Retry(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name="companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findall() {

        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();

        //Mapping the function to every object of jobs
        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    //If a mc is down, we send this message.
    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    private JobDTO convertToDTO(Job job){
        //  RestTemplate restTemplate = new RestTemplate();
        //  jobWithCompanyDTO.setJob(job);   To set the structure of JSON

//            Company company  = restTemplate.getForObject(
//                    "http://COMPANY-SERVICE:8081/companies/"+job.getCompanyId(),
//                    Company.class);

        Company company = companyClient.getCompany(job.getCompanyId());

//            ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                    "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<Review>>() {
//                    });


//        List<Review> reviews = reviewResponse.getBody();

        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job,company, reviews);
        return jobDTO;
    }
    
    
    
//    @Override
//    public void createJob(Job job) {
//        job.setId(nextId++);
//        jobs.add(job);
//    }

    @Override
    public void createJob(Job job) {
//        job.setId(nextId++);  No need of this as we are using  @GeneratedValue(strategy = GenerationType.IDENTITY)
//        in the Job.java file for id.
       jobRepository.save(job);
    }

//    @Override
//    public Job getJobById(Long id) {
//        for(Job job : jobs)
//        {
//            if(job.getId().equals(id)){
//                return job;
//            }
//        }
//        return null;
//    }

    @Override
    public JobDTO getJobById(Long id) {

        Job job = jobRepository.findById(id).orElse(null);
        return convertToDTO(job);

    }

//    @Override
//    public boolean deleteJobById(Long id) {
//        boolean deleted=false;
//        for(Job job : jobs)
//        {
//            if(job.getId().equals(id)){
//                jobs.remove(job);
//                deleted=true;
//            }
//        }
//        return deleted;
//    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
//        for(Job job : jobs)
//        {
//            if(job.getId().equals(id)){
//                job.setTitle(updatedJob.getTitle());
//                job.setDescription(updatedJob.getDescription());
//                job.setMinSalary(updatedJob.getMinSalary());
//                job.setMaxSalary(updatedJob.getMaxSalary());
//                job.setLocation(updatedJob.getLocation());
//                return true;
//            }
//        }
//        return false;
    Optional<Job> jobOptional = jobRepository.findById(id);
    if(jobOptional.isPresent()){
        Job job = jobOptional.get();
        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setMinSalary(updatedJob.getMinSalary());
        job.setMaxSalary(updatedJob.getMaxSalary());
        job.setLocation(updatedJob.getLocation());
        jobRepository.save(job);         //Very very important, we need to save the job so that PUT works!!
        return true;
    }
    return false;

    }
}
