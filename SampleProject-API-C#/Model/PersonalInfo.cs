namespace SampleProject.Model
{
    public class PersonalInfo {
        public PersonalInfo(string id)
        {
            this.Id = id;
        }
        
        public string Id { get; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
    }
}