import React from "react";
import Image, { ImageProps } from "next/image";
import styles from "./animatedImage.module.css";

interface AnimatedImageProps extends ImageProps {
  className?: string;
}

const AnimatedImage: React.FC<AnimatedImageProps> = ({ className = "", ...props }) => {
  return (
    <Image {...props} className={`${styles.animated} ${className}`.trim()} />
  );
};

export default AnimatedImage;
