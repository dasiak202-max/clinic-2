// INSY 8322 - Week 2 Class Practical: Clinic Patients in Plain JavaScript
// Rule: no push, splice or for loops. Spread, map, filter, find only.
// Run with:  node exercises/clinic.js

// 1) Array of 4 patient objects
const patients = [
  { id: 1, firstName: "Ama", lastName: "Uwase", dateOfBirth: "1999-04-12" },
  { id: 2, firstName: "Eric", lastName: "Mugisha", dateOfBirth: "1995-08-30" },
  { id: 3, firstName: "Grace", lastName: "Ingabire", dateOfBirth: "2001-01-17" },
  { id: 4, firstName: "Jean", lastName: "Habimana", dateOfBirth: "1988-11-05" },
];

// 2) addPatient - returns a NEW array (spread)
const addPatient = (list, patient) => [...list, patient];

// 3) updatePatient - map + ternary
const updatePatient = (list, updated) =>
  list.map((p) => (p.id === updated.id ? { ...p, ...updated } : p));

// 4) deletePatient - filter
const deletePatient = (list, id) => list.filter((p) => p.id !== id);

// 5) Print every patient as "Ama Uwase (1999-04-12)" - map + template literals
const formatPatients = (list) =>
  list.map((p) => `${p.firstName} ${p.lastName} (${p.dateOfBirth})`);

const printPatients = (title, list) => {
  console.log(`\n--- ${title} ---`);
  formatPatients(list).map((line) => console.log(line));
};

// ---- Test all 5 functions ----
printPatients("Original list", patients);

const afterAdd = addPatient(patients, {
  id: 5,
  firstName: "Diane",
  lastName: "Uwimana",
  dateOfBirth: "2000-06-21",
});
printPatients("After addPatient", afterAdd);

const afterUpdate = updatePatient(afterAdd, { id: 2, lastName: "Mugisha Kalisa" });
printPatients("After updatePatient (id 2)", afterUpdate);

const afterDelete = deletePatient(afterUpdate, 3);
printPatients("After deletePatient (id 3)", afterDelete);

// find example
const found = afterDelete.find((p) => p.id === 1);
console.log(`\nFound with find(): ${found.firstName} ${found.lastName}`);

console.log(`\nOriginal array unchanged? ${patients.length === 4}`);

// ---- Stretch: GET /api/patients from Spring Boot with fetch + await ----
// Start your Spring Boot clinic on port 8080 first.
const loadPatientsFromApi = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/patients");
    const data = await response.json();
    printPatients("Patients from Spring Boot API", data);
  } catch (error) {
    console.log("\n(Stretch) Spring Boot is not running - skipped API call.");
  }
};

loadPatientsFromApi();
