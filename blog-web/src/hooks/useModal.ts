import { useState } from "react";

interface ModalState {
    isOpen: boolean;
    content: React.ReactNode | null;
    onClose: () => void;
}

export const useModal = () => {
    const [modalState, setModalState] = useState<ModalState>({
        isOpen: false,
        content: null,
        onClose: () => {},
    });

    const openModal = (content: React.ReactNode, onClose: () => void) => {
        setModalState({
            isOpen: true,
            content,
            onClose,
        });
    };

    const closeModal = () => {
        if (modalState.onClose) {
            modalState.onClose();
        }
        setModalState({
            isOpen: false,
            content: null,
            onClose: () => {},
        });
    };

    return {
        modalState,
        openModal,
        closeModal,
    };
};