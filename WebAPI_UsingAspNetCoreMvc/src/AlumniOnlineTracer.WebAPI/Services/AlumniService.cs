using AlumniOnlineTracer.Core.Interfaces;
using AlumniOnlineTracer.Core.Model;
using AlumniOnlineTracer.WebAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AlumniOnlineTracer.WebAPI.Services
{
	public class AlumniService
	{
		private IAlumniRepository _repository;
		public AlumniService(IAlumniRepository repository)
		{
			_repository = repository;
		}

		internal IEnumerable<AlumnusViewModel> GetAllAlumni()
		{
			IEnumerable<AlumnusInfo> alumnusInfos = _repository.GetAll();
			List<AlumnusViewModel> alumnusModels = new List<AlumnusViewModel>();

			// TODO_JBOY:
			// 1. convert each alumnus info to alumnus model
			// 2. encrypt id
			// 3. convert id to base64 encoding

			foreach (var info in alumnusInfos)
			{
				alumnusModels.Add(new AlumnusViewModel
				{
					Id = info.Id.ToString(),
					FirstName = info.FirstName,
					LastName = info.LastName
				});
			}

			return alumnusModels;
		}

		internal AlumnusViewModel GetAlumnusModel(string id)
		{
			Guid idAsGuid;
			if (Guid.TryParse(id, out idAsGuid))
			{
				var info = _repository.GetAlumnusInfo(idAsGuid);

				return new AlumnusViewModel
				{
					Id = info.Id.ToString(),
					FirstName = info.FirstName,
					LastName = info.LastName
				};
			}

			return null;
		}

		internal string AddThenReturnID(CreateAlumnusViewModel model)
		{
			var info = new AlumnusInfo
			{
				FirstName = model.FirstName,
				LastName = model.LastName
			};
			_repository.Add(info);
			return info.Id.ToString();
		}
	}
}
