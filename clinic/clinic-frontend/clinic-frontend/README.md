# Clinic Management System - React Frontend

INSY 8322 - Web Technology and Internet | React Module

React + Vite frontend for the Spring Boot Clinic Management System.

## What is included

| Part | File | Status |
|------|------|--------|
| Offices page (list all offices) | `src/pages/OfficeList.jsx` | Done |
| Search by office name or number | `src/pages/OfficeList.jsx` | Done |
| Delete office (Action column) | `src/pages/OfficeList.jsx` | Done |
| Update button (Action column) | `src/pages/OfficeList.jsx` | Button only - not implemented (per instructions) |
| API calls with axios | `src/api/officeApi.js` | Done |
| Week 2 exercise (5 functions + stretch) | `exercises/clinic.js` | Done |

## Project structure

```
clinic-frontend/
├── exercises/
│   └── clinic.js          # Week 2 plain JavaScript exercise
├── public/
├── src/
│   ├── api/
│   │   └── officeApi.js   # GET all offices, DELETE office
│   ├── pages/
│   │   ├── OfficeList.jsx # Offices page: table, search, delete
│   │   └── OfficeList.css
│   ├── App.jsx
│   ├── index.css
│   └── main.jsx
├── index.html
├── package.json
└── vite.config.js
```

## How to run

1. Install Node.js (LTS) from https://nodejs.org and check it:
   ```bash
   node -v
   npm -v
   ```
2. Install the project libraries (run inside the `clinic-frontend` folder):
   ```bash
   npm install
   ```
3. Start your Spring Boot backend on port 8080.
4. Start the frontend:
   ```bash
   npm run dev
   ```
5. Open http://localhost:5173

## Backend requirements

The frontend expects these endpoints:

| Method | URL | Used for |
|--------|-----|----------|
| GET | `http://localhost:8080/api/offices` | Load all offices |
| DELETE | `http://localhost:8080/api/offices/{id}` | Delete one office |

Each office in the JSON should have `id`, `officeNumber` and `name`.
If your controller uses a different path, change `BASE_URL` in `src/api/officeApi.js`.

### Allow the frontend to call the API (CORS)

Add this annotation on your `OfficeController` (and later on the other controllers):

```java
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/offices")
public class OfficeController { ... }
```

Without it the browser blocks the request and the page shows
"Could not load offices".

## How the features work

- **Search** - filters the offices already loaded, as you type, by office name
  or office number. Uses `filter` (no loops).
- **Delete** - asks for confirmation, calls `DELETE /api/offices/{id}`, then
  removes the row from state with `filter`. If the backend returns an error
  (for example 404, or the office is still linked to doctors) the message is
  shown above the table.
- **Update** - shows a message only; implementation comes in a later exercise.

## Week 2 exercise

```bash
node exercises/clinic.js
```

Prints the patient list after add, update and delete. If Spring Boot is
running, it also prints `GET /api/patients` (stretch task).

## Push to Git

```bash
git init
git add .
git commit -m "Clinic frontend: offices page with search and delete, week 2 exercise"
git branch -M main
git remote add origin https://github.com/<your-username>/clinic-frontend.git
git push -u origin main
```

`node_modules` is ignored by `.gitignore` - do not push it.
