using AlumniOnlineTracer.Core.Interfaces;
using AlumniOnlineTracer.Core.Model;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;

namespace AlumniOnlineTracer.Controllers
{
	[Route("api/[controller]")]
	public class AlumniController : Controller
	{
		private IAlumniRepository _repository;
		public AlumniController(IAlumniRepository repository)
		{
			_repository = repository;
		}

		// GET api/Alumni
		[HttpGet]
		public IEnumerable<AlumnusInfo> Get()
		{
			return _repository.GetAll();

		}

		// GET api/Alumni/d74221f9-0708-4005-a553-e7d69e915f16
		[HttpGet("{id}")]
		public IActionResult GetById(Guid id)
		{
			var item = _repository.GetAlumnusInfo(id);
			if (item == null)
			{
				return NotFound();
			}
			return new ObjectResult(item);
		}

		// POST api/Alumni
		[HttpPost]
		public IActionResult Create([FromBody] AlumnusInfo item)
		{
			if (item == null)
			{
				return BadRequest();
			}
			_repository.Add(item);
			return CreatedAtRoute("GetById", new { id = item.Id }, item);
		}

		//// PUT api/Alumni/d74221f9-0708-4005-a553-e7d69e915f16
		//[HttpPut("{id}")]
		//public IActionResult Update(Guid id, [FromBody] AlumnusInfo item)
		//{
		//	if (item == null || item.Id != id)
		//	{
		//		return BadRequest();
		//	}

		//	var todo = _repository.GetAlumnusInfo(id);
		//	if (todo == null)
		//	{
		//		return NotFound();
		//	}

		//	_repository.Update(item);
		//	return new NoContentResult();
		//}

		//// DELETE api/Alumni/d74221f9-0708-4005-a553-e7d69e915f16
		//[HttpDelete("{id}")]
		//public void Delete(int id)
		//{
		//	_repository.Remove(id);
		//}
	}
}
