import React from "react";
import styles from "./featured.module.css";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";

const Featured = () => (
  <div className={styles.featuredBox}>
    <div className={styles.featuredRow}>
      <div className={styles.avatarContainer}>
        <AnimatedImage src="/profile-picture.png" alt="Gabriel's profile picture" width={160} height={160} className={styles.avatar} />
      </div>
      <div className={styles.textBlock}>
        <div className={styles.greeting}>Hi, I'm Gabriel</div>
        <div className={styles.subtitle}>A highly versatile and results-driven Senior Software Engineer with deep expertise in Java, Spring Boot, and Cloud (Azure & GCP). Passionate about building high-performance, scalable systems, I succeed in designing and developing solutions that handle intense data volumes while maintaining exceptional reliability and availability.</div>
      </div>
    </div>
  </div>
);

export default Featured;
