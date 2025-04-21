import React from 'react';
import styles  from './commentTile.module.css';
import { Comment } from '@/models/comment';
import Image from 'next/image';

const CommentTile = (comment: Comment) => {
    return (
        <div className={styles.commentTile}>
            <div className={styles.commentTileHeader}>
                <Image src={comment.author.profilePicture} alt="Avatar" className={styles.commentTileAvatar} width={60} height={60}/>
                <span className={styles.commentTileUsername}>{comment.author.name}</span>
                <span className={styles.commentTileDate}>{comment.creationDate}</span>
            </div>
            <div className={styles.commentTileContent}>
                <p>{comment.content}</p>
            </div>
        </div>
    );
};

export default CommentTile;