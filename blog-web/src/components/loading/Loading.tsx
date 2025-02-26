import React from 'react';
import styles from './loading.module.css';

const Loading: React.FC = () => {
    return (
        <div className={styles.loadingContainer}>
            <div className={styles.spinner}></div>
            <p className={styles.loadingText}>Loading...</p>
        </div>
    );
};

export default Loading;