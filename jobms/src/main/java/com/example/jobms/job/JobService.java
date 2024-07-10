package com.example.jobms.job;

import com.example.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {
    List<JobDTO> findall();
    void createJob(Job job);

    JobDTO getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJob(Long id, Job updatedJob);
}
