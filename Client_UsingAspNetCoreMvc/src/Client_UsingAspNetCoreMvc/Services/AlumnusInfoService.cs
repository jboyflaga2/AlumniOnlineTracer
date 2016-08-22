using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Net.Http;
using Newtonsoft.Json;
using AlumniOnlineTracer.Client.Model;

namespace AlumniOnlineTracer.Client.Services
{
	public class AlumnusInfoService // : IAlumnusInfoService
	{
		public async Task<IEnumerable<AlumnusModel>> GetAllAlumnusModels()
		{
			using (HttpClient client = new HttpClient())
			using (HttpResponseMessage response = await client.GetAsync("http://localhost:5555/api/alumni"))
			using (HttpContent content = response.Content)
			{
				// ... Read the string.
				string json = await content.ReadAsStringAsync();

				IEnumerable<AlumnusModel> model = JsonConvert.DeserializeObject<IEnumerable<AlumnusModel>>(json);
				return model;
			}
		}

		public AlumnusModel GetAlumnusModel(Guid id)
		{

			return null;
		}
	}
}
