import React, { useEffect, useState } from "react";
import { getAllOffices, deleteOffice } from "../api/officeApi";
import "./OfficeList.css";

function OfficeList() {
  const [offices, setOffices] = useState([]);     // all offices from the API
  const [search, setSearch] = useState("");       // what the user typed
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  // 1) Load offices once when the page opens
  useEffect(() => {
    loadOffices();
  }, []);

  const loadOffices = async () => {
    try {
      setLoading(true);
      const response = await getAllOffices();
      setOffices(response.data);
    } catch (error) {
      setMessage("Could not load offices. Is Spring Boot running on port 8080?");
    } finally {
      setLoading(false);
    }
  };

  // 2) SEARCH by office name or office number (filter - no loops)
  const keyword = search.trim().toLowerCase();
  const filteredOffices = offices.filter(
    (office) =>
      office.name?.toLowerCase().includes(keyword) ||
      String(office.officeNumber).includes(keyword)
  );

  // 3) DELETE: call the API, then remove it from state with filter
  const handleDelete = async (office) => {
    const ok = window.confirm(`Delete office "${office.name}" (No. ${office.officeNumber})?`);
    if (!ok) return;

    try {
      const response = await deleteOffice(office.id);
      setOffices(offices.filter((o) => o.id !== office.id));
      setMessage(typeof response.data === "string" ? response.data : "Office deleted successfully");
    } catch (error) {
      // e.g. 404 not found, or the office is still linked to doctors
      setMessage(error.response?.data || "Delete failed");
    }
  };

  // 4) UPDATE: not required yet - button only
  const handleUpdate = (office) => {
    alert(`Update for office ${office.officeNumber} is not implemented yet.`);
  };

  return (
    <div className="office-page">
      <h1>Offices</h1>

      <input
        className="search-box"
        type="text"
        placeholder="Search by office name or number..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      {message && <p className="message">{message}</p>}

      {loading ? (
        <p>Loading offices...</p>
      ) : (
        <table className="office-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Office Number</th>
              <th>Office Name</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {filteredOffices.length === 0 ? (
              <tr>
                <td colSpan="4" className="empty">No offices found</td>
              </tr>
            ) : (
              filteredOffices.map((office, index) => (
                <tr key={office.id}>
                  <td>{index + 1}</td>
                  <td>{office.officeNumber}</td>
                  <td>{office.name}</td>
                  <td>
                    <button className="btn btn-update" onClick={() => handleUpdate(office)}>
                      Update
                    </button>
                    <button className="btn btn-delete" onClick={() => handleDelete(office)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default OfficeList;
