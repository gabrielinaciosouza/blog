"use client";

import React, { useState } from 'react';
import styles from './addCommentModal.module.css';
import Button from '../button/Button';
import { TextEditor } from '../textEditor/TextEditor';
import Link from 'next/link';



const AddCommentModal = ({ slug }: { slug: string }) => {
    const [comment, setComment] = useState('');

    const handleAddComment = () => {

    };



    return (
        <div className={styles.modal}>
            <div className={styles.modalContent}>
                <h2 className={styles.title}>Add Comment</h2>
                <TextEditor content={comment} setContent={setComment} placeholder="Write your comment here" />
                <div className={styles.buttonContainer}>
                    <Button onClick={handleAddComment}>Submit</Button>
                    <Link href={`/posts/${slug}`}>
                        <Button>Close</Button>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default AddCommentModal;