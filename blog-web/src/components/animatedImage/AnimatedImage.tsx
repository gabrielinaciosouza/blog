import React from "react";
import Image, { ImageProps } from "next/image";
import styles from "./animatedImage.module.css";

interface AnimatedImageProps extends Omit<ImageProps, 'alt'> {
  alt: string;
  className?: string;
}

const AnimatedImage: React.FC<AnimatedImageProps> = ({ className = "", alt, ...props }) => {
  return (
    <Image {...props} alt={alt} className={`${styles.animated} ${className}`.trim()} />
  );
};

export default AnimatedImage;
