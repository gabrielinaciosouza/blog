import { useState } from "react";

interface ModalState {
    isOpen: boolean;
    content: React.ReactNode | null;
    onClose?: () => void;
}

export const useModal = () => {
    const [modalState, setModalState] = useState<ModalState>({
        isOpen: false,
        content: null,
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
            content: null
        });
    };

    return {
        modalState,
        openModal,
        closeModal,
    };
};