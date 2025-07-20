import React from "react";
import styles from "./modal.module.css";
import Button from "../button/Button";

interface ModalProps {
    isOpen: boolean;
    content: React.ReactNode;
    onClose: () => void;
}

const Modal: React.FC<ModalProps> = ({ isOpen, content, onClose }) => {
    if (!isOpen) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent} role="dialog" aria-modal="true">
                {content}
                <Button className={styles.closeModalButton} onClick={onClose}>
                    close
                </Button>
            </div>
        </div>
    );
};

export default Modal;