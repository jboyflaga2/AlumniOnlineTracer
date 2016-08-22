using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using AlumniOnlineTracer.Client.Services;
using AlumniOnlineTracer.Client.Model;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace AlumniOnlineTracer.Client.Controllers
{
	public class AlumnusInfoController : Controller
	{
		AlumnusInfoService _service;

		public AlumnusInfoController(AlumnusInfoService service)
		{
			_service = service;
		}

		// GET: /<controller>/
		public async Task<IActionResult> Index()
		{
			IEnumerable<AlumnusModel> model = await _service.GetAllAlumnusModels();
			return View(model);
		}
	}
}
