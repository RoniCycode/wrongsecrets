using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;

namespace ComprehensiveCSharpExample
{
    // Interface definition
    public interface ILogger
    {
        void Log(string message);
    }

    // ConsoleLogger implements ILogger
    public class ConsoleLogger : ILogger
    {
        public void Log(string message)
        {
            Console.WriteLine($"[LOG]: {message}");
        }
    }

    // Base class
    public abstract class Person
    {
        public string Name { get; set; }
        public int Age { get; set; }

        public abstract void DisplayInfo();
    }

    // Derived class
    public class Student : Person
    {
        public int Grade { get; set; }

        public override void DisplayInfo()
        {
            Console.WriteLine($"Student: {Name}, Age: {Age}, Grade: {Grade}");
        }
    }

    // Another derived class
    public class Teacher : Person
    {
        public string Subject { get; set; }

        public override void DisplayInfo()
        {
            Console.WriteLine($"Teacher: {Name}, Age: {Age}, Subject: {Subject}");
        }
    }

    // Utility class for file operations
    public static class FileHelper
    {
        public static async Task WriteToFileAsync(string filePath, string content)
        {
            using (StreamWriter writer = new StreamWriter(filePath, append: true))
            {
                await writer.WriteLineAsync(content);
            }
        }

        public static async Task<string> ReadFromFileAsync(string filePath)
        {
            if (!File.Exists(filePath))
            {
                throw new FileNotFoundException($"The file {filePath} does not exist.");
            }

            using (StreamReader reader = new StreamReader(filePath))
            {
                return await reader.ReadToEndAsync();
            }
        }
    }

    // Main program
    class Program
    {
        static async Task Main(string[] args)
        {
            ILogger logger = new ConsoleLogger();

            // Create some data
            List<Person> people = new List<Person>
            {
                new Student { Name = "Alice", Age = 20, Grade = 90 },
                new Student { Name = "Bob", Age = 22, Grade = 85 },
                new Teacher { Name = "Dr. Smith", Age = 45, Subject = "Mathematics" }
            };

            // Display information
            foreach (var person in people)
            {
                person.DisplayInfo();
            }

            // LINQ example: Find all students older than 21
            var olderStudents = people.OfType<Student>().Where(s => s.Age > 21);
            logger.Log("Students older than 21:");
            foreach (var student in olderStudents)
            {
                logger.Log($"{student.Name}, Age: {student.Age}, Grade: {student.Grade}");
            }

            // Serialize data to JSON
            string json = JsonSerializer.Serialize(people, new JsonSerializerOptions { WriteIndented = true });
            logger.Log("Serialized JSON:");
            logger.Log(json);

            // File I/O example
            string filePath = "people_data.txt";
            await FileHelper.WriteToFileAsync(filePath, json);
            logger.Log($"Data written to file: {filePath}");

            // Read the data back
            string fileContent = await FileHelper.ReadFromFileAsync(filePath);
            logger.Log("Data read from file:");
            logger.Log(fileContent);
        }
    }
}
