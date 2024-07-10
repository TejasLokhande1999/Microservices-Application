package com.example.companyms.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies()
    {
        return new ResponseEntity<>(companyService.getAllCompanies(),HttpStatus.OK);
    }

    @PostMapping("/companies")
    public ResponseEntity<String> createCompany(@RequestBody Company company){
        companyService.createCompany(company);
        return ResponseEntity.ok("Company created successfully");
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company){
        companyService.updateCompany(company,id);
        return new ResponseEntity<>("Company Updated Successfully", HttpStatus.OK);
    }


    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id){
        boolean deleted = companyService.deleteCompanyById(id);
        if(deleted)
        {
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id)
    {
        Company company = companyService.getCompanyById(id);
        if(company!=null){
            return new ResponseEntity<>(company,HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}


//Companies
//        GET /companies     Get in response body
//        PUT/companies/{id}
//        POST /companies
//        DELETE /companies/{id}
//        GET /companies/{id}
