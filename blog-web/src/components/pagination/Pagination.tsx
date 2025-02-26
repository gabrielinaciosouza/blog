"use client";

import React from "react";
import styles from "./pagination.module.css";
import { useRouter } from "next/navigation";
import Button from "../button/Button";

const Pagination = ({ page, hasPrev, hasNext } : {page: number, hasPrev: boolean, hasNext: boolean}) => {
    const router = useRouter();

    return (
        <div className={styles.container}>
            <Button
                disabled={!hasPrev}
                onClick={() => router.push(`?page=${+page - +1}`)}
            >
                Previous
            </Button>
            <Button
                disabled={!hasNext}
                onClick={() => router.push(`?page=${+page + +1}`)}
            >
                Next
            </Button>
        </div>
    );
};

export default Pagination;
