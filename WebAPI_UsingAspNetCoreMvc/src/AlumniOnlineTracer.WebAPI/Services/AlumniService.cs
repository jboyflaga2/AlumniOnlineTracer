using AlumniOnlineTracer.Core.Interfaces;
using AlumniOnlineTracer.Core.Model;
using AlumniOnlineTracer.WebAPI.Model;
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

		internal IEnumerable<AlumnusModel> GetAllAlumni()
		{
			IEnumerable<AlumnusInfo> alumnusInfos = _repository.GetAll();
			List<AlumnusModel> alumnusModels = new List<AlumnusModel>();

			// TODO_JBOY:
			// 1. convert each alumnus info to alumnus model
			// 2. encrypt id
			// 3. convert id to base64 encoding

			foreach (var info in alumnusInfos)
			{
				alumnusModels.Add(new AlumnusModel
				{
					Id = info.Id.ToString(),
					FirstName = info.FirstName,
					LastName = info.LastName
				});
			}

			return alumnusModels;
		}

		internal AlumnusModel GetAlumnusModel(string id)
		{
			Guid idAsGuid;
			if (Guid.TryParse(id, out idAsGuid))
			{
				var info = _repository.GetAlumnusInfo(idAsGuid);

				return new AlumnusModel
				{
					Id = info.Id.ToString(),
					FirstName = info.FirstName,
					LastName = info.LastName
				};
			}

			return null;
		}

		internal string AddThenReturnID(CreateAlumnusModel model)
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
