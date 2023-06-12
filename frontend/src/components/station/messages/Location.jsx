import { useState } from "react";
import "./Location.css";
import { useHangarContext } from "../HangarContext";
import { useNavigate } from "react-router-dom";

export default function Location({ location, availableShips }) {
  const [missionMenuToggle, setMissionMenuToggle] = useState(false);
  const hangar = useHangarContext();
  const navigate = useNavigate();

  console.log(hangar);

  function onSubmitMission(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const entries = [...formData.entries()];
    const details = entries.reduce((acc, entry) => {
      const [k, v] = entry;
      acc[k] = v;
      return acc;
    }, {});
    details.locationId = location.id;
    postMission(details);
  }

  function postMission(details) {
    fetch("/api/v1/mission", {
      method: "POST",
      headers: {
        "Content-Type" : "application/json"
      },
      body: JSON.stringify(details)
    })
      .then((res) => {
        if(res.ok) {
            return res.json();
        } else {
            throw new Error(res.status);
        }
      })
      .then((data) => {
        navigate(`/station/mission/${data.id}`);
      })
      .catch((e) => {
        console.error(e);});
  }

  return (
    <div className="location">
      <div className="loc-details">
        <img
          src="/planet.png"
          alt="planet"
          style={{ width: "100px", height: "100px" }}
        />
        <div className="loc-data">
          <div className="loc-name">Name: {location.name}</div>
          <div>Resource: {location.resourceType}</div>
          <div>Distance: {location.distanceFromStation} astronomical units</div>
        </div>
        {!missionMenuToggle && (
          <div className="loc-actions">
            {location.missionId === 0 ? (
              <button className="button" onClick={() => setMissionMenuToggle(true)}>
                Start mission
              </button>
            ) : (
              <button className="button" onClick={() => navigate(`/station/mission/${location.missionId}`)}>Check mission</button>
            )}
          </div>
        )}
      </div>
      {missionMenuToggle && (
        <form className="loc-mission-menu" onSubmit={onSubmitMission}>
          <div>
            <label htmlFor="shipId">Ship: </label>
            <select name="shipId" required>
              {availableShips.length > 0 ? availableShips.map(ship => <option key={ship.id} value={ship.id}>{ship.name}</option>) : <option disabled>No available ships</option>}
            </select>
          </div>
          <div>
            <label htmlFor="activityDuration">Mine for: </label>
            <select name="activityDuration" required>
              <option value={3600}>1 hour</option>
              <option value={7200}>2 hours</option>
              <option value={10800}>3 hours</option>
              <option value={14400}>4 hours</option>
              <option value={60}>DEMO</option>
            </select>
          </div>
          <div className="loc-mission-actions">
            <button className="button" type="submit">Start</button>
            <button className="button red" onClick={() => setMissionMenuToggle(false)}>
              Cancel
            </button>
          </div>
        </form>
      )}
    </div>
  );
}