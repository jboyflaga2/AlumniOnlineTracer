using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using SampleProject.Model;
using SampleProject.Repositories;

namespace SampleProject.Controllers
{
    [Route("api/[controller]")]
    public class PersonalInfoController : Controller
    {
        IPersonalInfoRepository _repository;

        public PersonalInfoController(IPersonalInfoRepository repository)
        {
            _repository = repository;
        }

        [HttpGet]
        public IEnumerable<PersonalInfo> Get()
        {
            return _repository.GetAll();
        }
        
        [HttpGet("{userId}")]
        public PersonalInfo Get(string userId)
        {
            return _repository.Get(userId);
        }

        [HttpPost]
        public void Post([FromBody]PersonalInfo personalInfo)
        {
            _repository.AddOrUpdate(personalInfo);
            _repository.Save();
        }

        [HttpPut("{userId}")]
        public void Put(string userId, [FromBody]PersonalInfo value)
        {
            
        }

        [HttpDelete("{userId}")]
        public void Delete(string userId)
        {
            _repository.Delete(userId);
            _repository.Save();
        }
    }
}
