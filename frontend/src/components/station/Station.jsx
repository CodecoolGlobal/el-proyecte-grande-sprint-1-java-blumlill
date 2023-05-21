import StationBar from "./stationbar/StationBar";
import "./Station.css";
import "./messages/Messages.css";
import { StationContext } from "./MessageContext";
import { StorageStationContext } from "./StorageContext";
import { HangarStationContext } from "./HangarContext";
import { Outlet, useOutletContext } from "react-router-dom";
import StationHeader from "./StationHeader";

const Station = () => {
  const context = useOutletContext();
  const user = context.user;
  const stationId = context.stationId;

  if (user === null || stationId === null) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <StationHeader></StationHeader>
      <div className="station">
        <StationContext>
          <StorageStationContext>
            <HangarStationContext>
              <StationBar></StationBar>
              <div className="message-log">
                <div className="messages">
                  <Outlet context={{ ...context }} />
                </div>
              </div>
            </HangarStationContext>
          </StorageStationContext>
        </StationContext>
      </div>
    </>
  );
};

export default Station;