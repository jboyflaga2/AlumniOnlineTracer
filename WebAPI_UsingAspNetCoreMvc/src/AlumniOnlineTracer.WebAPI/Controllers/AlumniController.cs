using AlumniOnlineTracer.Core.Interfaces;
using AlumniOnlineTracer.Core.Model;
using AlumniOnlineTracer.WebAPI.Models;
using AlumniOnlineTracer.WebAPI.Services;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;

namespace AlumniOnlineTracer.WebAPI.Controllers
{
	[Route("api/[controller]")]
	public class AlumniController : Controller
	{
		private AlumniService _service;
		public AlumniController(AlumniService service)
		{
			_service = service;
		}

		// GET api/Alumni
		[HttpGet]
		public IEnumerable<AlumnusViewModel> Get()
		{
			return _service.GetAllAlumni();

		}

		// GET api/Alumni/d74221f9-0708-4005-a553-e7d69e915f16
		[HttpGet("{id}")]
		public IActionResult GetById(string id)
		{
			var item = _service.GetAlumnusModel(id);
			if (item == null)
			{
				return NotFound();
			}
			return new ObjectResult(item);
		}

		// POST api/Alumni
		[HttpPost]
		public IActionResult Create([FromBody] CreateAlumnusViewModel item)
		{
			if (item == null)
			{
				return BadRequest();
			}
			string id = _service.AddThenReturnID(item);
			return CreatedAtRoute("GetById", new { Id = id }, item);
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
