import React, { useEffect, useRef, useState } from "react";
import { useGesture } from "@use-gesture/react";
import { Room } from "@mui/icons-material"

interface IMapViewProps {
    map: string;
    center: { x: number, y: number }
}

const MapView: React.FC<IMapViewProps> = ({ map, center }: IMapViewProps) => {
    const imgRef = useRef<HTMLImageElement>(null)
    const imgContainerRef = useRef<HTMLDivElement>(null)
    const [position,setPosition] = useState({ x: 150, y: 267 })
    const [scale,setScale] = useState(1)
    const [loaded,setLoaded] = useState(false)

    useEffect(()=>{

        const imgRect = imgRef.current?.getBoundingClientRect()

        const originalWidth = imgRef.current?.clientWidth
        const originalHeight = imgRef.current?.clientHeight
        const newWidth = imgRect!.width - originalWidth!
        const newHeight = imgRect!.height - originalHeight!


        const extraX = newWidth/2
        const extraY = newHeight/2

        setPosition({x:imgContainerRef.current!.clientWidth/2-(center.x*scale)+extraX,y:imgContainerRef.current!.clientHeight/2-(center.y*scale)+extraY})


    },[loaded,scale,center])

    useGesture({
        onDrag: ({ offset:[mx,my]}) => {
            setPosition({ x: mx, y: my })
        },
        onPinch: ({ offset: [d] }) => {
            setScale(d)
        },
        onDragEnd: () => {
            const newPostion = position
            const imgRect = imgRef.current?.getBoundingClientRect()
            const imgContainerRect = imgContainerRef.current?.getBoundingClientRect()

            const originalWidth = imgRef.current?.clientWidth
            const originalHeight = imgRef.current?.clientHeight
            const newWidth = imgRect!.width - originalWidth!
            const newHeight = imgRect!.height - originalHeight!

            console.log(imgRect!.width-imgContainerRect!.width)

            const extraX = newWidth/2
            const extraY = newHeight/2
            
            
            if(imgRect!.left! > imgContainerRect!.left!){
                newPostion.x = extraX
            }
            if(imgRect!.right! < imgContainerRect!.right!){
                newPostion.x = (imgContainerRect!.width! - imgRect!.width!)+extraX
            }
            if(imgRect!.top! > imgContainerRect!.top!){
                newPostion.y = extraY
            }
            if(imgRect!.bottom! < imgContainerRect!.bottom!){
                newPostion.y = (imgContainerRect!.height! - imgRect!.height!)+extraY
            }
            setPosition({...newPostion})
        }
    }, {
        target: imgRef,
        eventOptions: {
            passive: false,
        },
        drag:{
            preventDefault:true,
            from:()=>{
                return [position.x,position.y]
            },
            rubberband:true
        },
        pinch:{
            from:()=>{
                return [scale,scale]
            },
            scaleBounds:{
                min:0.5,
            }
        }
    });
    return (
        <div ref={imgContainerRef} style={{
            border: "1px solid blue",
            // width: "100vw",
            height: "80vh",
            overflow: "hidden",
            position: "relative",
            marginTop: "20px"
        }}>
            <div ref={imgRef} style={{ touchAction: "none", position: "absolute",left:position.x, top:position.y, transform:`scale(${scale})` }}>
            <img onLoad={()=>{setLoaded(true)}} src={map}  />
            {/* <Room style={{position:"absolute",left:center.x-40,top:center.y-50,color:"red",scale:'1'}} fontSize="large"/> */}
            </div>
        </div>
    );
}

export default MapView;