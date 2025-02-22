import React from "react";
import styles from "./modal.module.css";
import Button from "../button/Button";

const Modal: React.FC<{ children: React.ReactNode; onClose: () => void }> = ({ children, onClose }) => {
    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                {children}
                <Button className={styles.closeModalButton} onClick={onClose}>
                    close
                </Button>
            </div>
        </div>
    );
};

export default Modal;