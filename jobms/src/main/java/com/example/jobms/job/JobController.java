package com.example.jobms.job;

import com.example.jobms.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> findAll(){    //To get list of all jobs
        return ResponseEntity.ok(jobService.findall());
    }

    @PostMapping("/jobs")
    public ResponseEntity<String> createJob(@RequestBody  Job job)
    {
        jobService.createJob(job);
        return new ResponseEntity<>("Job added successfully",HttpStatus.OK);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id)
    {
        JobDTO jobDTO = jobService.getJobById(id);
        if(jobDTO !=null)
            return new ResponseEntity<>(jobDTO, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("/jobs/{id}")        //We will try to get the id parameter from UR
//    public String deleteJobById(@PathVariable Long id){
//        boolean deleted = jobService.deleteJobById(id);
//        if(deleted){
//            return "Deleted Successfully";
//        }else{
//            return "Job not found";
//        }
//    }
    @DeleteMapping("/jobs/{id}")        //We will try to get the id parameter from UR
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){
        boolean deleted = jobService.deleteJobById(id);
        if(deleted){
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //@PutMapping can also be written in form of @RequestMapping
    //@RequestMapping(value ="/jobs/{id}", method = RequestMethod.PUT)
    @PutMapping("/jobs/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id,
                                            @RequestBody Job updatedJob){
        boolean updated = jobService.updateJob(id,updatedJob);
        if(updated)
            return new ResponseEntity<>("Job Updated Successfully", HttpStatus.OK);
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

/*
Endpoints

GET /jobs : Get all jobs
GET /jobs/{id} : Get a specific job by ID
POST /jobs : Create a new job (Request body should contain the job details)
DELETE /jobs/{id} : Delete a specific job by ID
PUT /jobs/{id} : Update a specific job by ID (Request body should contains the job details

Example AP URLS:
GET {base_url}/jobs
GET {base_url}/jobs/1
POST {base_url}/jobs
DELETE {base_url}/jobs/`
PUT {base_url}/jobs/1


 */