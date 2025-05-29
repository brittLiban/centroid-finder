// server/utils/jobStore.js
const jobs = new Map();

/**
 * Called when a job is first created.
 * @param {string} jobId
 */
export function createJob(jobId) {
  jobs.set(jobId, { status: 'processing', result: null, error: null });
}

/**
 * Mark a job as successfully completed.
 * @param {string} jobId
 * @param {string} resultPath
 */
export function setJobDone(jobId, resultPath) {
  const job = jobs.get(jobId);
  if (job) {
    job.status = 'done';
    job.result = resultPath;
  }
}

/**
 * Mark a job as failed.
 * @param {string} jobId
 * @param {string} errorMsg
 */
export function setJobError(jobId, errorMsg) {
  const job = jobs.get(jobId);
  if (job) {
    job.status = 'error';
    job.error = errorMsg;
  }
}

/**
 * Fetch the job record, or undefined if not found.
 * @param {string} jobId
 */
export function getJob(jobId) {
  return jobs.get(jobId);
}
