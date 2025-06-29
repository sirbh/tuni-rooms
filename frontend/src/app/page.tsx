"use client";

import { AutoComplete } from "@/components/custom/autocomplete";
import MapView from "@/components/custom/mapview";
import { getMapByIdQuery, getMapSerachQuery, IMapOptions } from "@/queries";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";

export default function Home() {
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [defferedSearch, setDeferredSearch] = useState<string>("");
  const [selectedRoom, setSelectedRoom] = useState<IMapOptions>();

  useEffect(() => {
    const timer = setTimeout(() => {
      setDeferredSearch(searchQuery);
    }, 500);
    return () => clearTimeout(timer);
  }, [searchQuery]);

  const data = useQuery({
    queryKey: ["search", defferedSearch],
    queryFn: getMapSerachQuery(defferedSearch),
    enabled: defferedSearch.length > 0,
  });

  const map = useQuery({
    queryKey: ["getmap", selectedRoom],
    queryFn: selectedRoom ? getMapByIdQuery(selectedRoom) : undefined,
    enabled: !!selectedRoom,
  });

  const parsedPath = selectedRoom?.fileLocation
    ?.replace(/^\\|\\$/g, "")
    .split("\\");

  return (
    <div className="h-screen w-screen flex flex-col md:flex-row overflow-hidden md:overflow-auto bg-gray-50">
      {/* Left Column: Search + Info */}
      <div className="w-full md:w-96 bg-white shadow-md p-4 overflow-y-auto md:overflow-auto">
        <h1 className="text-2xl font-bold text-gray-800 mb-4">Room Finder</h1>

        <AutoComplete
          options={data.data || []}
          inputValue={searchQuery}
          onValueSelect={(opt) => setSelectedRoom(opt)}
          setInputValue={(value) => setSearchQuery(value)}
        />

        {selectedRoom && (
          <div className="mt-6 text-sm text-gray-700 space-y-1">
            <div><strong>Room:</strong> {selectedRoom.room}</div>
            <div><strong>Campus:</strong> {parsedPath?.[1]}</div>
            <div><strong>Building:</strong> {parsedPath?.[2]}</div>
            <div><strong>Floor:</strong> {parsedPath?.[3]}</div>
          </div>
        )}
      </div>

      {/* Right Column: Map */}
      <div className="flex-1 h-full overflow-hidden md:overflow-auto">
        {map.isLoading ? (
          <div className="h-full flex items-center justify-center">
            <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-blue-500"></div>
          </div>
        ) : map.data ? (
          <MapView
            map={`data:image/jpeg;base64,${map.data?.data.image}`}
            center={{
              x: map.data?.data.coordinates[0] || 0,
              y: map.data?.data.coordinates[1] || 0,
            }}
          />
        ) : (
          <div className="h-full flex items-center justify-center text-gray-400">
            Select a room to view the map.
          </div>
        )}
      </div>
    </div>
  );
}
