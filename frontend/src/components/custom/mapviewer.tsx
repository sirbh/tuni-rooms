import { useEffect, useRef, useState } from "react";


interface IMapViewProps {
    map: string;
    center: { x: number, y: number }
}

export default function MapViewer({ map, center }: IMapViewProps) {

    const imgRef = useRef<HTMLImageElement>(null)
    const containerRef = useRef<HTMLImageElement>(null)
    const [imgLoaded, setImgLoaded] = useState(false)
    const [x, setX] = useState(0)
    const [y, setY] = useState(0)

    const [scale, setScale] = useState(1);

    useEffect(() => {

        // console.log(imgRef.current?.getBoundingClientRect().width)
        console.log(containerRef.current?.getBoundingClientRect().width)

        // console.log(imgRef.current?.getBoundingClientRect().height)
        console.log(containerRef.current?.getBoundingClientRect().height)


        const containerW = containerRef.current?.getBoundingClientRect().width || 1;
        // const imgW = imgRef.current?.getBoundingClientRect().width || 1;

        const containerH = containerRef.current?.getBoundingClientRect().height || 1;
        // const imgH = imgRef.current?.getBoundingClientRect().height || 1;
        // const containerH = containerRef.current?.clientHeight || 1;
        // console.log(containerW / 2, imgW / 2);
        // console.log(containerH / 2, imgH / 2);

        console.log(center.x)
        console.log(center.y)

        const moveX = center.x - containerW / 2
        const moveY = center.y - containerH / 2
        // const moveX = (scale * imgW) / 2 - containerW / 2
        // const moveY = (scale * imgH) / 2 - containerH / 2

        console.log(moveX);
        console.log(moveY);
        // const moveh = 150 - containerH / 2

        // console.log(move, moveh);


        setX(moveX);
        setY(moveY);
        // setY(moveh);


    }, [center.x, center.y, imgLoaded, scale])




    // return <div ref={containerRef} className="w-[50vw] h-[40vh] overflow-hidden relative border-2 border-gray-300 ">
    //     <div ref={imgRef} className="absolute" style={{ left: `${-x}px` }}>
    //         <img alt="map" src={map} className="block" onLoad={()=>{setImgLoaded(true)}} />
    //     </div>
    // </div>
    return <div ref={containerRef} style={{ position: "relative", width: "50vw", height: "50vh", overflow: "hidden", border: "2px solid black" }} >
        <div ref={imgRef} className="absolute" style={{ left: `${-x}px`, top: `${-y}px` }} >
            <img alt="map" style={{scale: `${scale}`}} src={map} className="relative w-auto max-w-none max-h-none" onLoad={() => { console.log("I ran"); setImgLoaded(true) }} />
        </div>
    </div>


}