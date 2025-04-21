import ReactQuill from "react-quill-new";
import styles from "./textEditor.module.css";
import "react-quill-new/dist/quill.snow.css";
import { useRef } from "react";

interface TextEditorProps {
    quillRef?: React.RefObject<ReactQuill | null>,
    content: string,
    setContent: (value: string | ((val: string) => string)) => void,
    placeholder: string
}
export const TextEditor = (props: TextEditorProps) => {

    const quillRef = useRef<ReactQuill>(null);
    return (
        <ReactQuill
            ref={props.quillRef ?? quillRef}
            className={styles.textArea}
            value={props.content}
            onChange={props.setContent}
            placeholder={props.placeholder}
        />
    );
}