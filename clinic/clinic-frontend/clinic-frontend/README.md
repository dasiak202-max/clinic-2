# Clinic Frontend (React)

This is the React frontend for my Clinic Management System, done for INSY 8322 (Web Technology and Internet).
The backend is the Spring Boot project in this same repo, which I built in the earlier chapters.

So far I have done the Offices page. It shows all offices from the database, lets you search by office name or number, and lets you delete an office. The Update button is there but not working yet, because this exercise only asked for search and delete.

I also added `exercises/clinic.js`, which is the Week 2 plain JavaScript practice (add, update, delete and print patients without push, splice or for loops).

## What I used

React with Vite, axios to call the backend, and Node.js/npm. The backend is Spring Boot with PostgreSQL.

## Running it

The backend and the frontend have to run at the same time, so I use two terminals.

Backend (from the `clinic` folder):

```
.\mvnw spring-boot:run
```

It runs on port 8082. You can check it is working by opening http://localhost:8082/api/offices/all

Frontend (from `clinic-frontend/clinic-frontend`):

```
npm install
npm run dev
```

`npm install` is only needed the first time. Then open http://localhost:5173

To run the Week 2 exercise:

```
node exercises/clinic.js
```

## How it connects to the backend

All the API calls are in `src/api/officeApi.js`:

- `GET /api/offices/all` to load the offices
- `DELETE /api/offices/delete/{id}` to delete one

I had to add `@CrossOrigin(origins = "http://localhost:5173")` to `OfficeController`, otherwise the browser blocks the requests.

The page itself is `src/pages/OfficeList.jsx`. The offices are loaded once with `useEffect` and kept in state. Search just filters that list as you type. When you delete, it asks to confirm, calls the API, and then removes the row from the list with `filter`.

## Problems I ran into

- npm would not run in PowerShell at first. I fixed it with `Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned`.
- My backend uses port 8082, not 8080, so I had to change the URL in `officeApi.js`.
- I got a CORS error that was actually a 404, because I forgot `/all` at the end of the URL.

## Still to do

- Make the Update button work
- Add a form to create new offices
- Do the same pages for doctors, patients, specializations and appointments