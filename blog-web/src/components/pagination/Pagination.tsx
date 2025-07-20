"use client";

import React from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";

const Pagination = ({ page, hasPrev, hasNext }: { page: number, hasPrev: boolean, hasNext: boolean }) => {
    const router = useRouter();

    return (
        <div className="flex gap-4 justify-center items-center py-4">
            <Button
                variant="outline"
                disabled={!hasPrev}
                onClick={() => router.push(`?page=${page - 1}`)}
            >
                Previous
            </Button>
            <Button
                variant="outline"
                disabled={!hasNext}
                onClick={() => router.push(`?page=${page + 1}`)}
            >
                Next
            </Button>
        </div>
    );
};

export default Pagination;
