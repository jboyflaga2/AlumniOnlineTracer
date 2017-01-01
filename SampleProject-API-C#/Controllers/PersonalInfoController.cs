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
        
        [HttpGet("{id}")]
        public PersonalInfo Get(string id)
        {
            return _repository.Get(id);
        }

        [HttpPost]
        public void Post([FromBody]PersonalInfo personalInfo)
        {
            _repository.AddOrUpdate(personalInfo);
        }

        [HttpPut("{id}")]
        public void Put(string id, [FromBody]PersonalInfo value)
        {
            
        }

        [HttpDelete("{id}")]
        public void Delete(string id)
        {
            _repository.Delete(id);
        }
    }
}
